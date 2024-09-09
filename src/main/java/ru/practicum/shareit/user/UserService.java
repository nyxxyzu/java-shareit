package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
	UserDto create(User user);

	UserDto getUserById(long id);

	UserDto update(User user, long userId);

	Collection<UserDto> getAllUsers();

	void deleteUser(long userId);
}
