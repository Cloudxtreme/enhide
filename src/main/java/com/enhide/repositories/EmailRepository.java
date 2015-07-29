package com.enhide.repositories;

import com.enhide.models.persistent.Email;
import com.enhide.models.persistent.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author sales@enhide.com
 */
public interface EmailRepository extends CrudRepository<Email, Long> {

	@EntityGraph(value = "Email.withBody", type = EntityGraphType.LOAD)
	@Override
	Email findOne(Long id);

  @Query("select e from Email e join e.froms as f where f.user = ?1 and e.replyTo is null order by e.updatedAt desc")
  List<Email> fetchInbox(User user);
}
