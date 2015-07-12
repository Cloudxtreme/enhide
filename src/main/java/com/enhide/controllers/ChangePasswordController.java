package com.enhide.controllers;

import com.enhide.models.persistent.User;
import com.enhide.models.transitory.ChangePasswordRequest;
import com.enhide.services.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edwin.palathinkal
 */
@RestController
public class ChangePasswordController {

  @Autowired
  private ChangePasswordService changePasswordService;

  @RequestMapping(value = "/change_password", method = RequestMethod.PUT)
  public void changePassword(
          @AuthenticationPrincipal User user, 
          @RequestBody ChangePasswordRequest request) {
    changePasswordService.changePassword(user, request);
  }
}
