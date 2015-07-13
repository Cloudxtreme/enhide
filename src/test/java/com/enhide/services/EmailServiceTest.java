package com.enhide.services;

import com.enhide.Main;
import com.sun.jersey.api.client.ClientResponse;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

/**
 *
 * @author edwin.palathinkal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class EmailServiceTest {

  @Autowired
  private EmailService emailService;

  @Test
  public void testSendMimeMessage() {
    ClientResponse sendMimeMessage = emailService.sendMimeMessage(
            "Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>",
            Arrays.asList("edwinhere@gmail.com"),
            Arrays.asList(new String[]{}),
            Arrays.asList(new String[]{})
    );
    Assert.isTrue(sendMimeMessage.getStatus() == 200);
  }
}
