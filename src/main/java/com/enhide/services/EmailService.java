package com.enhide.services;

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
import java.util.List;
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

	public ClientResponse sendEncryptedMime(
		String from,
		List<String> tos,
		List<String> ccs,
		List<String> bccs,
		String subject,
		String pgp
	) throws IOException, ParseException {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", key));
		WebResource webResource = client.resource(resource);
		FormDataMultiPart form = new FormDataMultiPart();
		InputStream inputStream = createPgpMime(from, tos, ccs, bccs, subject, pgp);
		for (String to : tos) {
			form.field("to", to);
		}
		form.bodyPart(new StreamDataBodyPart("message", inputStream));
		return webResource
			.type(MediaType.MULTIPART_FORM_DATA_TYPE)
			.post(ClientResponse.class, form);
	}

	public ClientResponse sendSignedMime(
		String from,
		List<String> tos,
		List<String> ccs,
		List<String> bccs,
		String subject,
		String clearText,
		String singature
	) throws IOException, ParseException {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", key));
		WebResource webResource = client.resource(resource);
		FormDataMultiPart form = new FormDataMultiPart();
		InputStream inputStream = createSignedMime(from, tos, ccs, bccs, subject, clearText, singature);
		for (String to : tos) {
			form.field("to", to);
		}
		form.bodyPart(new StreamDataBodyPart("message", inputStream));
		return webResource
			.type(MediaType.MULTIPART_FORM_DATA_TYPE)
			.post(ClientResponse.class, form);
	}

	private InputStream createPgpMime(
		String from,
		List<String> tos,
		List<String> ccs,
		List<String> bccs,
		String subject,
		String pgp
	) throws IOException, ParseException {
		MessageBuilder builder = new DefaultMessageBuilder();
		Message message = builder.newMessage();
		{
			Header header = builder.newHeader();
			{
				AddressBuilder addressBuilder = AddressBuilder.DEFAULT;
				header.addField(Fields.from(addressBuilder.parseMailbox(from)));
				for (String to : tos) {
					header.addField(Fields.to(addressBuilder.parseAddress(to)));
				}
				for (String cc : ccs) {
					header.addField(Fields.cc(addressBuilder.parseAddress(cc)));
				}
				for (String bcc : bccs) {
					header.addField(Fields.bcc(addressBuilder.parseAddress(bcc)));
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
							return new ByteArrayInputStream(pgp.getBytes(StandardCharsets.US_ASCII));
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
		String from,
		List<String> tos,
		List<String> ccs,
		List<String> bccs,
		String subject,
		String clearText,
		String signature) throws IOException, ParseException {
		MessageBuilder builder = new DefaultMessageBuilder();
		Message message = builder.newMessage();
		{
			Header header = builder.newHeader();
			{
				AddressBuilder addressBuilder = AddressBuilder.DEFAULT;
				header.addField(Fields.from(addressBuilder.parseMailbox(from)));
				for (String to : tos) {
					header.addField(Fields.to(addressBuilder.parseAddress(to)));
				}
				for (String cc : ccs) {
					header.addField(Fields.cc(addressBuilder.parseAddress(cc)));
				}
				for (String bcc : bccs) {
					header.addField(Fields.bcc(addressBuilder.parseAddress(bcc)));
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
