package com.enhide.repositories;

import com.enhide.models.persistent.Body;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author sales@enhide.com
 */
public interface BodyRepository extends CrudRepository<Body, Long> {

	@Query("select b from Body b where email.id = ?")
	Body findByEmail(Long id);
}
