package com.enhide.controllers.admin;

import com.enhide.Main;
import com.enhide.BaseTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
public class UserControllerTest extends BaseTest {

	@Test
	public void usersEndpointAuthorized() throws Exception {
		// @formatter:off
		mvc.perform(get("/admin/users")
			.header("Authorization", "Bearer " + getAccessToken("admin", "spring")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
		// @formatter:on
	}

	@Test
	public void usersEndpointAccessDenied() throws Exception {
		// @formatter:off
		mvc.perform(get("/admin/users")
			.header("Authorization", "Bearer " + getAccessToken("test", "spring")))
			.andExpect(status().is(403));
		// @formatter:on
	}
}
