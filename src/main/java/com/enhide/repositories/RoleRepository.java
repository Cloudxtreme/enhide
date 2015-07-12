package com.enhide.repositories;

import com.enhide.models.persistent.Role;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author edwinhere@gmail.com
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {

	Role findByNameEquals(String roleName);
}
