package com.enhide.controllers;

import com.enhide.BaseTest;
import com.enhide.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 *
 * @author edwinhere@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class IsControllerTest extends BaseTest {

	@Test
	public void isAdmin() throws Exception {
		String accessToken = getAccessToken("admin", "spring");

		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
	}

	@Test
	public void isNotAdmin() throws Exception {
		String accessToken = getAccessToken("test", "spring");

		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
	}

	@Test
	public void isUser() throws Exception {
		String accessToken = getAccessToken("test", "spring");

		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
	}

	@Test
	public void isNotUser() throws Exception {
		String accessToken = getAccessToken("guest", "spring");

		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
	}

	@Test
	public void isGuest() throws Exception {
		String accessToken = getAccessToken("guest", "spring");

		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
	}

	@Test
	public void isNotGuest() throws Exception {
		mvc.perform(get("/is/guest")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void isAuthenticated() throws Exception {
		String accessToken = getAccessToken("guest", "spring");

		mvc.perform(get("/is/authenticated")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
	}

	@Test
	public void isNotAuthenticated() throws Exception {
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
}
