package com.enhide.repositories;

import com.enhide.models.persistent.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author edwinhere@gmail.com
 */
public interface UserRepository extends CrudRepository<User, Long> {

	User findByLogin(String login);
}
