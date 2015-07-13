package com.enhide.services;

import com.enhide.Main;
import com.sun.jersey.api.client.ClientResponse;
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
    ClientResponse sendMimeMessage = emailService.sendMimeMessage();
    Assert.isTrue(sendMimeMessage.getStatus() == 200);
  }
}
