package com.enhide.services;

import com.enhide.Main;
import com.enhide.BaseTest;
import com.enhide.models.persistent.Captcha;
import com.enhide.models.transitory.CaptchaImage;
import com.enhide.repositories.CaptchaRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
public class CaptchaServiceTest extends BaseTest {

	@Autowired
	private CaptchaRepository captchaRepository;

	@Autowired
	private CaptchaService captchaService;

	/**
	 * Test of generate method, of class CaptchaService.
	 */
	@Test
	public void testGenerate() {
		String solution = "test";
		CaptchaImage generate = captchaService.generate(solution);
		String hash = DigestUtils.sha256Hex(generate.getImage());
		Assert.isTrue(generate.getHash() != null && generate.getHash().equals(hash));
		Captcha findByHash = captchaRepository.findByHashEquals(hash);
		Assert.notNull(findByHash);
		Assert.hasText(findByHash.getHash());
		Assert.hasText(findByHash.getSolution());
		captchaRepository.delete(findByHash);
	}

	/**
	 * Test of check method, of class CaptchaService.
	 */
	@Test
	public void testCheck() {
		String solution = "test";
		CaptchaImage generate = captchaService.generate(solution);
		String hash = DigestUtils.sha256Hex(generate.getImage());
		Captcha input = new Captcha();
		input.setHash(hash);
		input.setSolution(solution);
		boolean check = captchaService.check(input);
		Assert.isTrue(check);
		Captcha findByHash = captchaRepository.findByHashEquals(hash);
		Assert.isNull(findByHash);
	}
}
