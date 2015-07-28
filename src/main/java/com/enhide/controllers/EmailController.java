package com.enhide.controllers;

import com.enhide.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sales@enhide.com
 */
@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
}
