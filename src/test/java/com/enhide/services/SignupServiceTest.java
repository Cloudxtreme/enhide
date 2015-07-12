package com.enhide.services;

import com.enhide.Main;
import com.enhide.BaseTest;
import com.enhide.models.persistent.Captcha;
import com.enhide.models.persistent.User;
import com.enhide.models.transitory.CaptchaImage;
import com.enhide.models.transitory.SignupRequest;
import org.apache.commons.codec.digest.DigestUtils;
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
 * @author edwinhere@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class SignupServiceTest extends BaseTest {

	@Autowired
	private SignupService signupService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CaptchaService captchaService;

	/**
	 * Test of signup method, of class SignupService.
	 */
	@Test
	public void testSignup() throws Exception {
		User user = new User();
		String userName = faker.name().firstName().toLowerCase();
		user.setLogin(userName);
		user.setName(faker.name().firstName());
		user.setPassword("spring");

		CaptchaImage image = captchaService.generate("test");
		String sha256Hex = DigestUtils.sha256Hex(image.getImage());
		Captcha captcha = new Captcha();
		captcha.setHash(sha256Hex);
		captcha.setSolution("test");

		SignupRequest request = new SignupRequest(user, captcha);

		signupService.signup(request);
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		Assert.notNull(userDetails);
		Assert.hasText(userDetails.getUsername());
		Assert.hasText(userDetails.getPassword());
		Assert.notEmpty(userDetails.getAuthorities());
		Assert.isTrue(passwordEncoder.matches("spring", userDetails.getPassword()));
	}
}
