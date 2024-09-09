package ru.practicum.shareit.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userStorage;


	@Autowired
	public UserServiceImpl(UserRepository userStorage) {
		this.userStorage = userStorage;

	}

	@Override
	@Transactional
	public UserDto create(RequestUserDto dto) {
		User user = UserMapper.mapToUser(dto);
		return UserMapper.mapToUserDto(userStorage.save(user));
	}

	@Override
	public UserDto getUserById(long id) {
		return userStorage.findById(id)
				.map(UserMapper::mapToUserDto)
				.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
	}

	@Override
	@Transactional
	public UserDto update(RequestUserDto dto, long userId) {
		User oldUser = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		if (dto.getName() != null) {
			oldUser.setName(dto.getName());
		}
		if (dto.getEmail() != null) {
			oldUser.setEmail(dto.getEmail());
		}
		return UserMapper.mapToUserDto(userStorage.save(oldUser));
	}

	@Override
	public Collection<UserDto> getAllUsers() {
		return userStorage.findAll().stream()
				.map(UserMapper::mapToUserDto)
				.toList();
	}

	@Override
	public void deleteUser(long userId) {
		userStorage.deleteById(userId);
	}
}
