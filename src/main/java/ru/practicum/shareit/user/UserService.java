package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
public class UserService {
	private final InMemoryUserRepository userStorage;


	@Autowired
	public UserService(InMemoryUserRepository userStorage) {
		this.userStorage = userStorage;

	}

	public UserDto create(User user) {
		return UserMapper.mapToUserDto(userStorage.create(user));
	}

	public UserDto getUserById(long id) {
		return UserMapper.mapToUserDto(userStorage.getUserById(id));
	}

	public UserDto update(User user, long userId) {
		return UserMapper.mapToUserDto(userStorage.update(user, userId));
	}

	public Collection<UserDto> getAllUsers() {
		return userStorage.getAllUsers().stream()
				.map(UserMapper::mapToUserDto)
				.toList();
	}

	public void deleteUser(long userId) {
		userStorage.deleteUser(userId);
	}
}
