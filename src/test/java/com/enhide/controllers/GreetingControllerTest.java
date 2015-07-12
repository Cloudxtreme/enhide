package com.enhide.controllers;

import com.enhide.BaseTest;
import com.enhide.Main;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author edwinhere@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class GreetingControllerTest extends BaseTest {

	@Test
	public void greetingUnauthorized() throws Exception {
		// @formatter:off
		mvc.perform(get("/greeting")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error", is("unauthorized")));
		// @formatter:on
	}

	@Test
	public void greetingAuthorized() throws Exception {
		String accessToken = getAccessToken("admin", "spring");

		// @formatter:off
		mvc.perform(get("/greeting")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.content", is("Hello, Administrator!")));
		// @formatter:on

		// @formatter:off
		mvc.perform(get("/greeting")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.content", is("Hello, Administrator!")));
		// @formatter:on

		// @formatter:off
		mvc.perform(get("/greeting")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(3)))
			.andExpect(jsonPath("$.content", is("Hello, Administrator!")));
		// @formatter:on
	}

}
