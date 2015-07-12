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
public class LogoutControllerTest extends BaseTest {

	@Test
	public void testUserLogoutTest() throws Exception {
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/guest"))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/user"))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/admin"))
			.andExpect(status().isUnauthorized());

		String accessToken = getAccessToken("test", "spring");

		mvc.perform(get("/is/authenticated")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
		mvc.perform(get("/revoke")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void guestUserLogoutTest() throws Exception {
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/guest"))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/user"))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/admin"))
			.andExpect(status().isUnauthorized());

		String accessToken = getAccessToken("guest", "spring");

		mvc.perform(get("/is/authenticated")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
		mvc.perform(get("/revoke")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void adminUserLogoutTest() throws Exception {
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/guest"))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/user"))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/admin"))
			.andExpect(status().isUnauthorized());

		String accessToken = getAccessToken("admin", "spring");

		mvc.perform(get("/is/authenticated")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isForbidden());
		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/revoke")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk());
		mvc.perform(get("/is/authenticated")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/guest")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/user")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
		mvc.perform(get("/is/admin")
			.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized());
	}
}
