package com.enhide.services;

import com.enhide.models.persistent.Role;
import com.enhide.models.persistent.User;
import com.enhide.models.transitory.SignupRequest;
import com.enhide.repositories.RoleRepository;
import com.enhide.repositories.UserRepository;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author edwinhere@gmail.com
 */
@Service
public class SignupService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CaptchaService captchaService;

	public void signup(SignupRequest request) throws Exception {
		if (captchaService.check(request.getCaptcha())) {
			User user = request.getUser();
			String password = user.getPassword();
			String encodedPassword = passwordEncoder.encode(password);
			user.setPassword(encodedPassword);
			Role guest = roleRepository.findByNameEquals("ROLE_GUEST");
			HashSet<Role> set = new HashSet<>();
			set.add(guest);
			user.setRoles(set);
			userRepository.save(user);
		} else {
			throw new Exception("Unsolved captcha exception");
		}
	}
}
