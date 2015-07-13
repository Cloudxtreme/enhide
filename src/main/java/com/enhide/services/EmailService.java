package com.enhide.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import java.io.File;
import java.util.List;
import javax.ws.rs.core.MediaType;
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

  public ClientResponse sendMimeMessage(
          String from,
          List<String> tos,
          List<String> ccs,
          List<String> bccs) {
    Client client = Client.create();
    client.addFilter(new HTTPBasicAuthFilter("api", key));
    WebResource webResource = client.resource(resource);
    FormDataMultiPart form = new FormDataMultiPart();
    form.field("from", from);
    for (String to : tos) {
      form.field("to", to);
    }
    for (String cc : ccs) {
      form.field("cc", cc);
    }
    for (String bcc : bccs) {
      form.field("bcc", bcc);
    }
    File mimeFile = new File("message.mime");
    form.bodyPart(new FileDataBodyPart("message", mimeFile,
            MediaType.APPLICATION_OCTET_STREAM_TYPE));
    return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).
            post(ClientResponse.class, form);
  }
}
