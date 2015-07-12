package com.enhide;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsNull.notNullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author edwinhere@gmail.com
 */
public class BaseTest {

	@Autowired
	WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	protected MockMvc mvc;

	protected Faker faker;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilter(springSecurityFilterChain)
			.build();

		faker = new Faker();
	}

	public String getAccessToken(String username, String password) throws Exception {
		String authorization = "Basic " + new String(Base64Utils.encode("clientapp:123456".getBytes()));
		String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

		// @formatter:off
		String content = mvc
			.perform(
				post("/oauth/token")
				.header("Authorization", authorization)
				.contentType(
					MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", username)
				.param("password", password)
				.param("grant_type", "password")
				.param("scope", "read write")
				.param("client_id", "clientapp")
				.param("client_secret", "123456"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.access_token", is(notNullValue())))
			.andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
			.andExpect(jsonPath("$.refresh_token", is(notNullValue())))
			.andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
			.andExpect(jsonPath("$.scope", is(equalTo("read write"))))
			.andReturn().getResponse().getContentAsString();

		// @formatter:on
		return content.substring(17, 53);
	}
}
