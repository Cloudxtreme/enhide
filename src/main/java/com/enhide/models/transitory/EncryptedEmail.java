package com.enhide.models.transitory;

import java.util.List;

/**
 *
 * @author sales@enhide.com
 */
public class EncryptedEmail {

	private String from;
	private List<String> tos;
	private List<String> ccs;
	private List<String> bccs;
	private String subject;
	private String message;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTos() {
		return tos;
	}

	public void setTos(List<String> tos) {
		this.tos = tos;
	}

	public List<String> getCcs() {
		return ccs;
	}

	public void setCcs(List<String> ccs) {
		this.ccs = ccs;
	}

	public List<String> getBccs() {
		return bccs;
	}

	public void setBccs(List<String> bccs) {
		this.bccs = bccs;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
