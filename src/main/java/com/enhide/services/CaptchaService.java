package com.enhide.services;

import com.enhide.models.persistent.Captcha;
import com.enhide.models.transitory.CaptchaImage;
import com.enhide.repositories.CaptchaRepository;
import com.github.cage.GCage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

/**
 *
 * @author edwinhere@gmail.com
 */
@Service
public class CaptchaService {

	@Autowired
	private CaptchaRepository captchaRepository;

	private GCage cage;

	public CaptchaService() {
		cage = new GCage();
	}

	public CaptchaImage generate(String solution) {
		if (StringUtils.isBlank(solution)) {
			solution = cage.getTokenGenerator().next();
		}
		byte[] image = cage.draw(solution);
		byte[] bytes = Base64.encode(image);
		String base64Image = "data:image/jpeg;base64," + new String(bytes);
		String hash = DigestUtils.sha256Hex(base64Image);
		Captcha captcha = new Captcha();
		captcha.setHash(hash);
		captcha.setSolution(solution);
		captchaRepository.save(captcha);
		return new CaptchaImage(base64Image);
	}

	public boolean check(Captcha input) {
		if (input == null
						|| StringUtils.isBlank(input.getHash())
						|| StringUtils.isBlank(input.getSolution())) {
			return false;
		}

		Captcha captcha = captchaRepository.findByHashEquals(input.getHash());

		if (captcha == null
						|| StringUtils.isBlank(captcha.getHash())
						|| StringUtils.isBlank(captcha.getSolution())) {
			return false;
		}

		boolean result = StringUtils.equalsIgnoreCase(captcha.getSolution(), input.getSolution());
		captchaRepository.delete(captcha);
		return result;
	}
}
