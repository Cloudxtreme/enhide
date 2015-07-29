package com.enhide.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 *
 * @author edwinhere@gmail.com
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends
	ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "restservice";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		// @formatter:off
		resources
			.resourceId(RESOURCE_ID);
		// @formatter:on
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests()
			.antMatchers("/actuator/**").hasRole("ADMIN")
			.antMatchers("/admin/users").hasRole("ADMIN")
			.antMatchers("/is/admin").hasRole("ADMIN")
			.antMatchers("/is/user").hasRole("USER")
			.antMatchers("/is/guest").hasRole("GUEST")
			.antMatchers("/is/authenticated").authenticated()
			.antMatchers("/revoke").authenticated()
			.antMatchers("/greeting").authenticated()
			.antMatchers("/inbox").authenticated()
			.antMatchers("/change_password").authenticated()
			.antMatchers("/signup").anonymous();
		// @formatter:on
	}

}
