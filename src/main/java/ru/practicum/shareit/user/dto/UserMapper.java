package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;


public class UserMapper {

	public static UserDto mapToUserDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		return dto;
	}
}