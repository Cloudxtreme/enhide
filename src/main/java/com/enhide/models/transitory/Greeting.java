package com.enhide.models.transitory;

/**
 *
 * @author edwinhere@gmail.com
 */
public class Greeting {

	private final long id;

	private final String content;

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

}
