package com.enhide.controllers;

import com.enhide.BaseTest;
import com.enhide.Main;
import com.enhide.models.transitory.CaptchaImage;
import com.enhide.services.CaptchaService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.startsWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

/**
 *
 * @author sales@enhide.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class SignupControllerTest extends BaseTest {

	@Autowired
	private CaptchaService captchaService;

	@Test
	public void testSignup() throws Exception {
		CaptchaImage ci = captchaService.generate("test");
		String hash = DigestUtils.sha256Hex(ci.getImage());
		MvcResult result = mvc.perform(post("/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(
				"{\n"
				+ "    \"user\": {\n"
				+ "        \"name\": \"Edwin\",\n"
				+ "        \"login\": \"edwin\",\n"
				+ "        \"password\": \"spring\"\n"
				+ "    },\n"
				+ "    \"captcha\": {\n"
				+ "        \"hash\": \"" + hash + "\",\n"
				+ "        \"solution\": \"test\"\n"
				+ "    }\n"
				+ "}")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void testCaptcha() throws Exception {
		mvc.perform(get("/captcha")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.image", startsWith("data:image/jpeg;base64,")));
	}

}
