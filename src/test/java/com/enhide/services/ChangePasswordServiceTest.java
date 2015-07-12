package com.enhide.services;

import com.enhide.BaseTest;
import com.enhide.Main;
import com.enhide.models.persistent.User;
import com.enhide.models.transitory.ChangePasswordRequest;
import com.enhide.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class ChangePasswordServiceTest extends BaseTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChangePasswordService changePasswordService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void testChangePassword() {
		User user = userRepository.findByLogin("test");
		ChangePasswordRequest request = new ChangePasswordRequest();
		request.setNewPassword("123456789");
		changePasswordService.changePassword(user, request);
		UserDetails userDetails = userDetailsService.loadUserByUsername("test");
		Assert.notNull(userDetails);
		Assert.hasText(userDetails.getUsername());
		Assert.hasText(userDetails.getPassword());
		Assert.notEmpty(userDetails.getAuthorities());
		Assert.isTrue(passwordEncoder.matches("123456789", userDetails.getPassword()));
	}
}
