package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

	private final UserService userService;
	private RequestUserDto userDto;


	private RequestUserDto makeUserDto(String name, String email) {
		RequestUserDto dto = new RequestUserDto();
		dto.setName(name);
		dto.setEmail(email);
		return dto;
	}

	@BeforeEach
	void createUserDto() {
		userDto = makeUserDto("TestName", "testemail@mail.com");
	}

	@Test
	void testCreate() {
		UserDto user = userService.create(userDto);
		assertThat(user.getId(), notNullValue());
		assertThat(user.getName(), equalTo(userDto.getName()));
		assertThat(user.getEmail(), equalTo(userDto.getEmail()));
	}

	@Test
	void testGetById() {
		UserDto user = userService.create(userDto);
		UserDto user2 = userService.getUserById(user.getId());
		assertThat(user2.getId(), equalTo(user.getId()));
		assertThat(user2.getName(), equalTo(user.getName()));
		assertThat(user2.getEmail(), equalTo(user.getEmail()));

	}

	@Test
	void testUpdate() {
		UserDto user = userService.create(userDto);
		RequestUserDto dto = makeUserDto(null, "newtestemail@mail.com");
		UserDto updatedUser = userService.update(dto, user.getId());
		assertThat(user.getName(), equalTo(updatedUser.getName()));
		assertThat(dto.getEmail(), equalTo(updatedUser.getEmail()));
	}

	@Test
	void testGetAllUsers() {
		userService.create(userDto);
		userService.create(makeUserDto("name2", "email2@mail.com"));
		Collection<UserDto> users = userService.getAllUsers();
		assertThat(users.size(), equalTo(2));
	}

	@Test
	void testDeleteUser() {
		UserDto user = userService.create(userDto);
		Collection<UserDto> users = userService.getAllUsers();
		assertThat(users.size(), equalTo(1));
		userService.deleteUser(user.getId());
		users = userService.getAllUsers();
		assertThat(users.size(), equalTo(0));
	}

}
