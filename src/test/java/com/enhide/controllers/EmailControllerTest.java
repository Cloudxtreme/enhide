package com.enhide.controllers;

import com.enhide.BaseTest;
import com.enhide.Main;
import com.enhide.models.persistent.Address;
import com.enhide.models.persistent.Email;
import com.enhide.models.persistent.User;
import com.enhide.repositories.BodyRepository;
import com.enhide.repositories.EmailRepository;
import com.enhide.repositories.UserRepository;
import com.enhide.services.EmailService;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
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
  private EmailService emailService;

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
    List<Email> inbox = emailService.inbox(findByLogin);
    String accessToken = getAccessToken("admin", "spring");
    MvcResult andReturn = mvc.perform(get("/inbox")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk()).andReturn();
    String contentAsString = andReturn.getResponse().getContentAsString();
    System.out.println("#####################################################");
    System.out.println(contentAsString);
    System.out.println("#####################################################");
  }
}
