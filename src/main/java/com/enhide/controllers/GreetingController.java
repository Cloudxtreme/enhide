package com.enhide.controllers;

import com.enhide.models.transitory.Greeting;
import com.enhide.models.persistent.User;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edwinhere@gmail.com
 */
@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public Greeting greeting(@AuthenticationPrincipal User user) {
		return new Greeting(counter.incrementAndGet(), String.format(template, user.getName()));
	}

}
