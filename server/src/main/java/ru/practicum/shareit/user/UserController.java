package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;


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
	public Collection<UserDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping
	public UserDto create(@RequestBody RequestUserDto dto) {
		UserDto createdUser = userService.create(dto);
		log.info("Created user {}", createdUser.toString());
		return createdUser;
	}

	@PatchMapping("/{id}")
	public UserDto update(@RequestBody RequestUserDto dto, @PathVariable("id") long id) {
		UserDto updatedUser = userService.update(dto, id);
		log.info("Updated user {}", updatedUser.toString());
		return updatedUser;
	}

	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable("id") long id) {
		return userService.getUserById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
	}
}
