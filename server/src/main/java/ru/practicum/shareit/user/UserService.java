package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
	UserDto create(RequestUserDto dto);

	UserDto getUserById(long id);

	UserDto update(RequestUserDto dto, long userId);

	Collection<UserDto> getAllUsers();

	void deleteUser(long userId);
}
