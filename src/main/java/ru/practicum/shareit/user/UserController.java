package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

import java.util.Collection;


@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

	private final UserService userService;


	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public Collection<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping
	public User create(@Validated({BasicInfo.class, AdvancedInfo.class}) @RequestBody User user) {
		User createdUser = userService.create(user);
		log.info("Created user {}", createdUser.toString());
		return createdUser;
	}

	@PatchMapping("/{id}")
	public User update(@Validated({AdvancedInfo.class}) @RequestBody User newUser, @PathVariable("id") long id) {
		User updatedUser = userService.update(newUser, id);
		log.info("Updated user {}", updatedUser.toString());
		return updatedUser;
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") long id) {
		return userService.getUserById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
	}
}
