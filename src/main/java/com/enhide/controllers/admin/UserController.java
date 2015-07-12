package com.enhide.controllers.admin;

import com.enhide.models.persistent.User;
import com.enhide.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edwinhere@gmail.com
 */
@RestController
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping("/admin/users")
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}

}
