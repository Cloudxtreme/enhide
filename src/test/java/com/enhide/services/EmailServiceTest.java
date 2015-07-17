package com.enhide.services;

import com.enhide.Main;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.Arrays;
import org.apache.james.mime4j.field.address.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

/**
 *
 * @author edwin.palathinkal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@Test
	public void testSendMimeMessage() throws IOException, ParseException {
		String pgp = "-----BEGIN PGP MESSAGE-----\n"
			+ "Version: GnuPG v2\n"
			+ "\n"
			+ "hQIMA6whYqiToljLARAAhSOp1Ni7rqujc3OV/v0iJmIcGK53iqp78XOzp16xDaJD\n"
			+ "A/WRLbJIXmUOqiKoAqEWxbWyJzPRuLOK6InEzt8vBknO01pRZS+YiYNd+i4+T7Uw\n"
			+ "FWHsGJL8wHmEo0yz2ZGBpnmiuy7vY7tCXi8ccavlbfqsr6KiBylVpMZcOxVpTq3m\n"
			+ "BX3crObgb5wvWD5qmsTD+7ofVVld2EzPIQc/ax8imSMuu1S3RVwdiYBdCMUtlvQ1\n"
			+ "XqfMM9H7oW1pUdW2qqh5NnxkSUhp3T3Vb38eXSXcJngcQCZrFDIQPECqRIl3LzHM\n"
			+ "vTKNo8b5zKMLPHXngL+DxBfIcVWSl5m2ei0QNE3K05HcaeZQ9vNyTn5Ic+bd0sHt\n"
			+ "YRUtRDFcnL8+DUzAHANcP8SpYIQdVsiPrKmbtMG/Uzna7hnR4zxGseMk7GTEBszf\n"
			+ "I2e3hJ4SB3xeQU9Pv/Ar6dtZPGIemG9El3/jpovbjKSa8XqOsZSmp+R99e8znh48\n"
			+ "Kl+ndW1/LmSu4XN07+0KopOq8e6A3jEJXjx1uNcBMjWO7D4PXYTqcrFtNZcDbX1z\n"
			+ "dn5b1aO5CQEcKaE/3znigTefM0saFWFEa9P0mKKwqQWWOOe8900AW7BxX4P/0t2c\n"
			+ "vXkonmZjlm3RQA0bLfLOIJnY16F+kVSiMHl+kOe5TwLVRhP4Or9TbaNEv06IGJfS\n"
			+ "wEQB5FdwKRl4vE1KDh3VRqQODoM0wGKXhCG0FmOFRIUFrwgL8FQOXGX28dIqjLtj\n"
			+ "VjKahnxOw3fgdo+ln+OfZfj6nxmJ9TnpW2J+bWeoyWzjtRa9yVTAQe2HFYtM/JDS\n"
			+ "Z1CDDMU32sG7jMWyyaJGuteXKLlJcugohvxY7Z9OtkRTGcmoa50tNlxA2ogi/S+/\n"
			+ "UfwaHat4I9J5bHqwwJ3jjW3W4I/EwHKyq7EsMKN9aKQ+nn+moxO3IzHzuBpmu5qG\n"
			+ "tlfqIxMuPTEQmnSgHjm7SPaqqD8OC+14nkRAmwcLWOI4HjZlIVvRT/j1dcEimRRd\n"
			+ "PWImUmpX9UrTzfjtJ0kj6ug6q/Pe7w==\n"
			+ "=fssi\n"
			+ "-----END PGP MESSAGE-----\n";

		ClientResponse sendMimeMessage = emailService.sendMimeMessage(
			"Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>",
			Arrays.asList("edwinhere@gmail.com"),
			Arrays.asList(new String[]{}),
			Arrays.asList(new String[]{}),
			"Hello",
			pgp
		);
		Assert.isTrue(sendMimeMessage.getStatus() == 200);
	}
}
