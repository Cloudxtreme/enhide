package com.enhide.services;

import com.enhide.models.persistent.Email;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.MessageWriter;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.SingleBody;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.field.Fields;
import org.apache.james.mime4j.field.address.AddressBuilder;
import org.apache.james.mime4j.field.address.ParseException;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.message.DefaultMessageWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.enhide.models.persistent.Address;
import com.enhide.models.persistent.Body;
import com.enhide.models.transitory.SendRequest;
import com.enhide.repositories.EmailRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 *
 * @author edwin.palathinkal
 */
@Service
public class EmailService {

	@Value(value = "${mailgun.key}")
	private String key;

	@Value(value = "${mailgun.resource}")
	private String resource;

	@Autowired
	private EmailRepository emailRepository;

	public ClientResponse send(SendRequest sendRequest) throws IOException, ParseException, Exception {
		Email email = sendRequest.getEmail();
		Assert.notNull(email, "Cannot send blank email");
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", key));
		WebResource webResource = client.resource(resource);
		FormDataMultiPart form = new FormDataMultiPart();
		InputStream inputStream = null;
		String message = sendRequest.getMessage();
		String clearText = sendRequest.getClearText();
		String signature = sendRequest.getSignature();
		if (StringUtils.isNotBlank(message)) {
			inputStream = createEncryptedMime(
				email.getFroms(),
				email.getTos(),
				email.getCcs(),
				email.getBccs(),
				email.getSubject(),
				message);
		} else if (StringUtils.isNotBlank(clearText)
			&& StringUtils.isNotBlank(signature)) {
			inputStream = createSignedMime(
				email.getFroms(),
				email.getTos(),
				email.getCcs(),
				email.getBccs(),
				email.getSubject(),
				clearText,
				signature);
		} else {
			throw new Exception(
				"Sorry we do not allow the sending of unsigned cleartext emails. ");
		}

		for (Address to : email.getTos()) {
			form.field("to", to.getValue());
		}
		form.bodyPart(new StreamDataBodyPart("message", inputStream));
		ClientResponse post = webResource
			.type(MediaType.MULTIPART_FORM_DATA_TYPE)
			.post(ClientResponse.class, form);
		if (post.getStatus() == 200) {
			String value = IOUtils.toString(inputStream);
			Body body = new Body();
			body.setValue(value);
			email.setBody(body);
			emailRepository.save(email);
		}
		return post;
	}

	private InputStream createEncryptedMime(
		Set<Address> froms,
		Set<Address> tos,
		Set<Address> ccs,
		Set<Address> bccs,
		String subject,
		String encrypted
	) throws IOException, ParseException {
		MessageBuilder builder = new DefaultMessageBuilder();
		Message message = builder.newMessage();
		{
			Header header = builder.newHeader();
			{
				AddressBuilder addressBuilder = AddressBuilder.DEFAULT;
				for (Address from : froms) {
					header.addField(Fields.from(addressBuilder.parseMailbox(from.getValue())));
				}
				for (Address to : tos) {
					header.addField(Fields.to(addressBuilder.parseAddress(to.getValue())));
				}
				for (Address cc : ccs) {
					header.addField(Fields.cc(addressBuilder.parseAddress(cc.getValue())));
				}
				for (Address bcc : bccs) {
					header.addField(Fields.bcc(addressBuilder.parseAddress(bcc.getValue())));
				}
				header.addField(Fields.subject(subject));
				Map<String, String> map = new HashMap<>();
				map.put("protocol", "application/pgp-encrypted");
				map.put("boundary", "lICdWpALSxNuvFoHT9h2kd6OuMwufkLpT");
				header.addField(Fields.contentType("multipart/encrypted", map));
				message.setHeader(header);
			}
			Multipart multipart = builder.newMultipart("");
			{
				multipart.setPreamble("This is an OpenPGP/MIME encrypted message (RFC 4880 and 3156)");
				BodyPart version = new BodyPart();
				{
					Header versionHeader = builder.newHeader();
					versionHeader.addField(Fields.contentType("application/pgp-encrypted"));
					version.setHeader(versionHeader);
					version.setBody(new SingleBody() {
						@Override
						public InputStream getInputStream() throws IOException {
							return new ByteArrayInputStream("Version: 1".getBytes(StandardCharsets.US_ASCII));
						}
					});
					multipart.addBodyPart(version);
				}
				BodyPart octets = new BodyPart();
				{
					Header octetHeader = builder.newHeader();
					Map<String, String> octetMap = new HashMap<>();
					octetMap.put("name", "encrypted.asc");
					octetHeader.addField(Fields.contentType("application/octet-stream", octetMap));
					Map<String, String> dispositionMap = new HashMap<>();
					dispositionMap.put("filename", "encrypted.asc");
					octetHeader.addField(Fields.contentDisposition("inline", octetMap));
					octets.setHeader(octetHeader);
					octets.setBody(new SingleBody() {
						@Override
						public InputStream getInputStream() throws IOException {
							return new ByteArrayInputStream(encrypted.getBytes(StandardCharsets.US_ASCII));
						}
					});
					multipart.addBodyPart(octets);
				}
			}
			multipart.setParent(message);
			message.setBody(multipart);
			MessageWriter writer = new DefaultMessageWriter();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			writer.writeMessage(message, outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
	}

	private ByteArrayInputStream createSignedMime(
		Set<Address> froms,
		Set<Address> tos,
		Set<Address> ccs,
		Set<Address> bccs,
		String subject,
		String clearText,
		String signature) throws IOException, ParseException {
		MessageBuilder builder = new DefaultMessageBuilder();
		Message message = builder.newMessage();
		{
			Header header = builder.newHeader();
			{
				AddressBuilder addressBuilder = AddressBuilder.DEFAULT;
				for (Address from : froms) {
					header.addField(Fields.from(addressBuilder.parseMailbox(from.getValue())));
				}
				for (Address to : tos) {
					header.addField(Fields.to(addressBuilder.parseAddress(to.getValue())));
				}
				for (Address cc : ccs) {
					header.addField(Fields.cc(addressBuilder.parseAddress(cc.getValue())));
				}
				for (Address bcc : bccs) {
					header.addField(Fields.bcc(addressBuilder.parseAddress(bcc.getValue())));
				}
				header.addField(Fields.subject(subject));
				Map<String, String> map = new HashMap<>();
				map.put("micalg", "pgp-sha256");
				map.put("protocol", "application/pgp-signature");
				map.put("boundary", "UBFBPcp4fjBXlbJE0rkpJs89BT9lk0OxI");
				header.addField(Fields.contentType("multipart/signed", map));
				message.setHeader(header);
			}
			Multipart multipart = builder.newMultipart("");
			{
				multipart.setPreamble("This is an OpenPGP/MIME encrypted message (RFC 4880 and 3156)");
				BodyPart plainText = new BodyPart();
				{
					Header plainTextHeader = builder.newHeader();
					Map<String, String> map = new HashMap<>();
					map.put("charset", "utf-8");
					plainTextHeader.addField(Fields.contentType("text/plain", map));
					plainTextHeader.addField(Fields.contentTransferEncoding("quoted-printable"));
					plainText.setHeader(plainTextHeader);
					plainText.setBody(new TextBody() {

						@Override
						public String getMimeCharset() {
							return StandardCharsets.UTF_8.name();
						}

						@Override
						public Reader getReader() throws IOException {
							return new StringReader(clearText);
						}

						@Override
						public InputStream getInputStream() throws IOException {
							return new ByteArrayInputStream(clearText.getBytes(StandardCharsets.UTF_8));
						}
					});
					multipart.addBodyPart(plainText);
				}
				BodyPart octets = new BodyPart();
				{
					Header octetHeader = builder.newHeader();
					Map<String, String> octetMap = new HashMap<>();
					octetMap.put("name", "signature.asc");
					octetHeader.addField(Fields.contentType("application/pgp-signature", octetMap));
					Map<String, String> dispositionMap = new HashMap<>();
					dispositionMap.put("filename", "signature.asc");
					octetHeader.addField(Fields.contentDisposition("attachment", octetMap));
					octets.setHeader(octetHeader);
					octets.setBody(new SingleBody() {
						@Override
						public InputStream getInputStream() throws IOException {
							return new ByteArrayInputStream(signature.getBytes(StandardCharsets.US_ASCII));
						}
					});
					multipart.addBodyPart(octets);
				}
			}
			multipart.setParent(message);
			message.setBody(multipart);
			MessageWriter writer = new DefaultMessageWriter();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			writer.writeMessage(message, outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
	}
}
