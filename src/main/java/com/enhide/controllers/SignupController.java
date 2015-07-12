package com.enhide.controllers;

import com.enhide.models.transitory.CaptchaImage;
import com.enhide.models.transitory.SignupRequest;
import com.enhide.services.CaptchaService;
import com.enhide.services.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edwinhere@gmail.com
 */
@RestController
public class SignupController {

	@Autowired
	private SignupService signupService;

	@Autowired
	private CaptchaService captchaService;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void signup(@RequestBody SignupRequest request) throws Exception {
		signupService.signup(request);
	}

	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public CaptchaImage captcha() {
		return captchaService.generate(null);
	}
}
