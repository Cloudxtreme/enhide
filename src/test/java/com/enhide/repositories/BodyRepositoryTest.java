package com.enhide.repositories;

import com.enhide.Main;
import com.enhide.models.persistent.Address;
import com.enhide.models.persistent.Body;
import com.enhide.models.persistent.Email;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

/**
 *
 * @author sales@enhide.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Main.class)
public class BodyRepositoryTest {

	@Autowired
	private BodyRepository bodyRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Test
	public void testFindByEmailId() {
		Email email = new Email();
		Body body = new Body();
		body.setValue("Test");
		email.setBody(body);
		body.setEmail(email);

		Set<Address> froms = new HashSet<>();
		froms.addAll((Collection<Address>) Arrays.asList(Address.fromString("Excited User <mailgun@sandboxe61c04d2f5d7416c9137dc01dc3fd3b4.mailgun.org>")));
		Set<Address> tos = new HashSet<>();
		tos.addAll((Collection<Address>) Arrays.asList(Address.fromString("edwinhere@gmail.com")));
		Set<Address> ccs = new HashSet<>();
		ccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));
		Set<Address> bccs = new HashSet<>();
		bccs.addAll((Collection<Address>) Arrays.asList(new Address[]{}));

		email.setFroms(froms);
		email.setTos(tos);
		email.setCcs(ccs);
		email.setBccs(bccs);

		Email saved = emailRepository.save(email);
		Assert.isNull(bodyRepository.findByEmail(-1L));
		Body findByEmail = bodyRepository.findByEmail(saved.getId());
		Assert.isTrue(null != findByEmail);
		Assert.isTrue(StringUtils.equals(findByEmail.getValue(), "Test"));
	}
}
