package com.enhide.repositories;

import com.enhide.models.persistent.Email;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author sales@enhide.com
 */
public interface EmailRepository extends CrudRepository<Email, Long> {

	@EntityGraph(value = "Email.withBody", type = EntityGraphType.LOAD)
	@Override
	Email findOne(Long id);
}
