package com.enhide.controllers;

import com.enhide.BaseTest;
import com.enhide.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author edwin.palathinkal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class ChangePasswordControllerTest extends BaseTest {

	@Test
	public void testChangePassword() throws Exception {
		changePassword("spring", "123456789");
		changePassword("123456789", "spring");
	}

	private void changePassword(String oldPassword, String newPassword) throws Exception {
		String accessToken = getAccessToken("test", oldPassword);
		mvc.perform(put("/change_password")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + accessToken)
			.content("{\n"
				+ "  \"newPassword\": \"" + newPassword + "\"\n"
				+ "}")
		).andExpect(status().isOk());
	}
}
