package com.enhide.models.transitory;

import com.enhide.models.persistent.Email;

/**
 *
 * @author sales@enhide.com
 */
public class SendRequest {

	private Email email;
	private String message;
	private String clearText;
	private String signature;

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getClearText() {
		return clearText;
	}

	public void setClearText(String clearText) {
		this.clearText = clearText;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
