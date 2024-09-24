package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	private final UserClient userClient;

	@GetMapping
	public ResponseEntity<Object> getAllUsers() {
		return userClient.getAllUsers();
	}

	@PostMapping
	public ResponseEntity<Object> create(@Validated({BasicInfo.class, AdvancedInfo.class}) @RequestBody RequestUserDto dto) {
		return userClient.create(dto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> update(@Validated({AdvancedInfo.class}) @RequestBody RequestUserDto dto, @PathVariable("id") long id) {
		return userClient.update(dto, id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable("id") long id) {
		return userClient.getUserById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
		return userClient.deleteUser(id);
	}
}
