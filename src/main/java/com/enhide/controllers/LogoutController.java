package com.enhide.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edwinhere@gmail.com
 */
@RestController
public class LogoutController {

	@Autowired
	private ConsumerTokenServices tokenServices;

	@RequestMapping(value = "/revoke")
	public void revoke(@RequestHeader("Authorization") String value) {
		String[] splits = StringUtils.split(value, " ");
		String token = null;
		if (splits != null) {
			token = splits[splits.length - 1];
		}
		if (token != null) {
			tokenServices.revokeToken(token);
		}
	}
}
