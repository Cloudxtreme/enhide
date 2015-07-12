package com.enhide.services;

import com.enhide.models.persistent.User;
import com.enhide.models.transitory.ChangePasswordRequest;
import com.enhide.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author edwin.palathinkal
 */
@Service
public class ChangePasswordService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  public void changePassword(User user, ChangePasswordRequest request) {
    User findByLogin = userRepository.findByLogin(user.getLogin());
    String newPassword = request.getNewPassword();
    String encodedPassword = passwordEncoder.encode(newPassword);
    findByLogin.setPassword(encodedPassword);
    userRepository.save(findByLogin);
  }
}
