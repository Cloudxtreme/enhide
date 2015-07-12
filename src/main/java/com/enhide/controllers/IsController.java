package com.enhide.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edwinhere@gmail.com
 */
@RestController
@RequestMapping("/is")
public class IsController {

	@RequestMapping("/admin")
	public void isAdmin() {
	}

	@RequestMapping("/user")
	public void isUser() {
	}

	@RequestMapping("/guest")
	public void isGuest() {
	}

	@RequestMapping("/authenticated")
	public void isAuthenticated() {
	}
}
