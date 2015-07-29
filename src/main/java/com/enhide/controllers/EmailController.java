package com.enhide.controllers;

import com.enhide.models.persistent.Email;
import com.enhide.models.persistent.User;
import com.enhide.models.transitory.SendRequest;
import com.enhide.services.EmailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sales@enhide.com
 */
@RestController
public class EmailController {

  @Autowired
  private EmailService emailService;

  @RequestMapping("/inbox")
  public List<Email> inbox(@AuthenticationPrincipal User user) {
    return emailService.inbox(user);
  }

  @RequestMapping("/send")
  public void send(@AuthenticationPrincipal User user,
          @RequestBody SendRequest sendRequest) throws Exception {
    emailService.send(sendRequest);
  }
}
