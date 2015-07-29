package com.enhide.controllers;

import com.enhide.BaseTest;
import com.enhide.Main;
import com.enhide.models.persistent.Address;
import com.enhide.models.persistent.Email;
import com.enhide.models.persistent.User;
import com.enhide.models.transitory.SendRequest;
import com.enhide.repositories.EmailRepository;
import com.enhide.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author edwin.palathinkal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class EmailControllerTest extends BaseTest {

  @Autowired
  private EmailRepository emailRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void inboxUnauthorized() throws Exception {
    mvc.perform(get("/inbox")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error", is("unauthorized")));
  }

  @Test
  public void inboxAuthorized() throws Exception {
    User findByLogin = userRepository.findByLogin("admin");
    Address fromString = Address.fromString(
            "Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>");
    fromString.setUser(findByLogin);
    Set<Address> froms = new HashSet<>();
    froms.addAll(
            (Collection<Address>) Arrays.asList(fromString));
    Set<Address> tos = new HashSet<>();
    tos.addAll((Collection<Address>) Arrays.asList(Address.fromString("edwinhere@gmail.com")));
    Set<Address> ccs = new HashSet<>();
    ccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));
    Set<Address> bccs = new HashSet<>();
    bccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));

    Email email = new Email();
    email.setFroms(froms);
    email.setTos(tos);
    email.setCcs(ccs);
    email.setBccs(bccs);
    String subject = "Inbox Controller Test";
    email.setSubject(subject);

    emailRepository.save(email);
    String accessToken = getAccessToken("admin", "spring");
    mvc.perform(get("/inbox")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].froms[0].value", startsWith("Excited User")))
            .andExpect(jsonPath("$[0].tos[0].value", startsWith("edwinhere")))
            .andExpect(jsonPath("$[0].subject", is("Inbox Controller Test")));
  }

  @Test
  public void sendUnauthorized() throws Exception {
    mvc.perform(get("/send")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error", is("unauthorized")));
  }

  @Test
  public void sendAuthorized() throws Exception {
    String accessToken = getAccessToken("admin", "spring");
    ObjectMapper mapper = new ObjectMapper();

    User findByLogin = userRepository.findByLogin("admin");
    Address fromString = Address.fromString(
            "Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>");
    fromString.setUser(findByLogin);
    Set<Address> froms = new HashSet<>();
    froms.addAll(
            (Collection<Address>) Arrays.asList(fromString));
    Set<Address> tos = new HashSet<>();
    tos.addAll((Collection<Address>) Arrays.asList(Address.fromString("edwinhere@gmail.com")));
    Set<Address> ccs = new HashSet<>();
    ccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));
    Set<Address> bccs = new HashSet<>();
    bccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));

    Email email = new Email();
    email.setFroms(froms);
    email.setTos(tos);
    email.setCcs(ccs);
    email.setBccs(bccs);
    String subject = "Inbox Controller Test";
    email.setSubject(subject);

    SendRequest sendRequest = new SendRequest();
    String pgp = "-----BEGIN PGP MESSAGE-----\n"
            + "Version: GnuPG v2\n"
            + "\n"
            + "hQIMA6whYqiToljLARAAhSOp1Ni7rqujc3OV/v0iJmIcGK53iqp78XOzp16xDaJD\n"
            + "A/WRLbJIXmUOqiKoAqEWxbWyJzPRuLOK6InEzt8vBknO01pRZS+YiYNd+i4+T7Uw\n"
            + "FWHsGJL8wHmEo0yz2ZGBpnmiuy7vY7tCXi8ccavlbfqsr6KiBylVpMZcOxVpTq3m\n"
            + "BX3crObgb5wvWD5qmsTD+7ofVVld2EzPIQc/ax8imSMuu1S3RVwdiYBdCMUtlvQ1\n"
            + "XqfMM9H7oW1pUdW2qqh5NnxkSUhp3T3Vb38eXSXcJngcQCZrFDIQPECqRIl3LzHM\n"
            + "vTKNo8b5zKMLPHXngL+DxBfIcVWSl5m2ei0QNE3K05HcaeZQ9vNyTn5Ic+bd0sHt\n"
            + "YRUtRDFcnL8+DUzAHANcP8SpYIQdVsiPrKmbtMG/Uzna7hnR4zxGseMk7GTEBszf\n"
            + "I2e3hJ4SB3xeQU9Pv/Ar6dtZPGIemG9El3/jpovbjKSa8XqOsZSmp+R99e8znh48\n"
            + "Kl+ndW1/LmSu4XN07+0KopOq8e6A3jEJXjx1uNcBMjWO7D4PXYTqcrFtNZcDbX1z\n"
            + "dn5b1aO5CQEcKaE/3znigTefM0saFWFEa9P0mKKwqQWWOOe8900AW7BxX4P/0t2c\n"
            + "vXkonmZjlm3RQA0bLfLOIJnY16F+kVSiMHl+kOe5TwLVRhP4Or9TbaNEv06IGJfS\n"
            + "wEQB5FdwKRl4vE1KDh3VRqQODoM0wGKXhCG0FmOFRIUFrwgL8FQOXGX28dIqjLtj\n"
            + "VjKahnxOw3fgdo+ln+OfZfj6nxmJ9TnpW2J+bWeoyWzjtRa9yVTAQe2HFYtM/JDS\n"
            + "Z1CDDMU32sG7jMWyyaJGuteXKLlJcugohvxY7Z9OtkRTGcmoa50tNlxA2ogi/S+/\n"
            + "UfwaHat4I9J5bHqwwJ3jjW3W4I/EwHKyq7EsMKN9aKQ+nn+moxO3IzHzuBpmu5qG\n"
            + "tlfqIxMuPTEQmnSgHjm7SPaqqD8OC+14nkRAmwcLWOI4HjZlIVvRT/j1dcEimRRd\n"
            + "PWImUmpX9UrTzfjtJ0kj6ug6q/Pe7w==\n"
            + "=fssi\n"
            + "-----END PGP MESSAGE-----\n";
    sendRequest.setMessage(pgp);
    sendRequest.setEmail(email);

    String json = mapper.writeValueAsString(sendRequest);

    mvc.perform(get("/send")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer " + accessToken)
      .content(json))
      .andExpect(status().isOk());
  }
}
