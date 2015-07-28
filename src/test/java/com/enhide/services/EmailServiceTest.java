package com.enhide.services;

import com.enhide.Main;
import com.enhide.models.persistent.Address;
import com.enhide.models.persistent.Body;
import com.enhide.models.persistent.Email;
import com.enhide.models.transitory.SendRequest;
import com.enhide.repositories.BodyRepository;
import com.enhide.repositories.EmailRepository;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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

	@Autowired
	private BodyRepository bodyRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Test
	public void testSendMimeMessage() throws IOException, ParseException, Exception {
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

		Set<Address> froms = new HashSet<>();
		froms.addAll((Collection<Address>) Arrays.asList(Address.fromString("Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>")));
		Set<Address> tos = new HashSet<>();
		tos.addAll((Collection<Address>) Arrays.asList(Address.fromString("edwinhere@gmail.com")));
		Set<Address> ccs = new HashSet<>();
		ccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));
		Set<Address> bccs = new HashSet<>();
		bccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));

		SendRequest sendRequest = new SendRequest();
		Email email = new Email();
		email.setFroms(froms);
		email.setTos(tos);
		email.setCcs(ccs);
		email.setBccs(bccs);
		email.setSubject("multipart/encrypted Test");
		sendRequest.setEmail(email);
		sendRequest.setMessage(pgp);

		Pair<ClientResponse, Email> pair = emailService.send(sendRequest, true);
		ClientResponse clientResponse = pair.getLeft();
		Email saved = pair.getRight();
		Assert.isTrue(clientResponse.getStatus() == 200);
		Email findOne = emailRepository.findOne(saved.getId());
		Body findByEmail = bodyRepository.findByEmail(saved.getId());
		Assert.isTrue(findOne != null);
		Assert.isTrue(findByEmail != null);
		Assert.isTrue(StringUtils.equals(findOne.getBody().getValue(), findByEmail.getValue()));
	}

	@Test
	public void testSendSignedMime() throws IOException, ParseException, Exception {
		String clearText = "Hi, I'm Testing signed unencrypted PGP/MIME\n"
			+ "\n"
			+ "Edwin\n\n";
		String pgp = "-----BEGIN PGP SIGNATURE-----\n"
			+ "Version: GnuPG v2\n"
			+ "\n"
			+ "iQIcBAEBCAAGBQJVpNL3AAoJEK5vpWWqPiw4bh4QAOBUuhAMF3ECbk/Puyrw9iEW\n"
			+ "XhDmP7yY9sV3mKN3vCEcxi5KzQ0/mYqV6XopgseEMPmbtkIDZ6VcUPve2UUf/Ief\n"
			+ "ZHS9k7ZbQXrPSU59lEhI551tagzAXvnuZnDQQMh4uXrmmub0TCcPRJHgZ7MxO80g\n"
			+ "2V4nmHs4oPnjF0sfKqD44G797+fCtOyFugM9YymEmlCitW6MiEVd5tZx3oCAFHhI\n"
			+ "0ibrIbhpj7eYo2DJ235kbABR9UWNM5JeJk+klPjpgcQae76eO58LQD9FrDH4+x4b\n"
			+ "st3Lgk//h9CTVSaZ0d0nOEL2xcIT37nngmw4v8jmRSW3S+R+xk8kzGqesARaz3DZ\n"
			+ "c1VC8bNEp3Fc/PqFCZrJAJDaOCI4fhC92/dvPrXdHec1SJSu/BIRkgoubOUIl8wb\n"
			+ "dt52/grDjxVG6jNRXHYEPyEScCbEGgroHdUb2eA9a+sVxnXMKA1g9ovaUxM4YdTI\n"
			+ "9NV0li5OQ/7uk0HyxNcmTzZ+76mToM6gA4kurxxUDuHQ+R/1RXxhzGYUZG2sqypE\n"
			+ "xgvA9Dt/7KLdOtYu5yYfz9RJjqZvYZmNFlufEG2PFxMiAkDowfvfp9mzGFlFqXth\n"
			+ "a/rASXilWte4kIEoygxVBFqF5CKpe8UwVA+0HyBHgLb/8/TYigVfmxFWACGby1Y5\n"
			+ "OhJBd4TuNKom7utNEHnT\n"
			+ "=Sc1/\n"
			+ "-----END PGP SIGNATURE-----\n";

		Set<Address> froms = new HashSet<>();
		froms.addAll((Collection<Address>) Arrays.asList(Address.fromString("Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>")));
		Set<Address> tos = new HashSet<>();
		tos.addAll((Collection<Address>) Arrays.asList(Address.fromString("edwinhere@gmail.com")));
		Set<Address> ccs = new HashSet<>();
		ccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));
		Set<Address> bccs = new HashSet<>();
		bccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));

		SendRequest sendRequest = new SendRequest();
		Email email = new Email();
		email.setFroms(froms);
		email.setTos(tos);
		email.setCcs(ccs);
		email.setBccs(bccs);
		email.setSubject("multipart/signed Test");
		sendRequest.setEmail(email);
		sendRequest.setClearText(clearText);
		sendRequest.setSignature(pgp);

		Pair<ClientResponse, Email> pair = emailService.send(sendRequest, true);
		ClientResponse clientResponse = pair.getLeft();
		Email saved = pair.getRight();
		Assert.isTrue(clientResponse.getStatus() == 200);
		Email findOne = emailRepository.findOne(saved.getId());
		Body findByEmail = bodyRepository.findByEmail(saved.getId());
		Assert.isTrue(findOne != null);
		Assert.isTrue(findByEmail != null);
		Assert.isTrue(StringUtils.equals(findOne.getBody().getValue(), findByEmail.getValue()));
	}

	@Test
	public void testBlank() {
	}
}
