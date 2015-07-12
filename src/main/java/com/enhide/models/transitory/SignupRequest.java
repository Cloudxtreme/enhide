package com.enhide.models.transitory;

import com.enhide.models.persistent.Captcha;
import com.enhide.models.persistent.User;

/**
 *
 * @author edwinhere@gmail.com
 */
public class SignupRequest {

	private User user;
	private Captcha captcha;

	public SignupRequest() {
	}

	public SignupRequest(User user, Captcha captcha) {
		this.user = user;
		this.captcha = captcha;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Captcha getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}
}
