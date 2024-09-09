package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	ObjectMapper mapper;

	@MockBean
	UserService userService;

	@Autowired
	private MockMvc mvc;

	private UserDto userDto = new UserDto();

	@BeforeEach
	void makeUserDto() {
		userDto.setId(1L);
		userDto.setName(("name"));
		userDto.setEmail("email@email.com");
	}

	@Test
	void createUserTest() throws Exception {
		when(userService.create(any()))
				.thenReturn(userDto);

		mvc.perform(post("/users")
				.content(mapper.writeValueAsString(userDto))
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(userDto.getName())))
				.andExpect(jsonPath("$.email", is(userDto.getEmail())));
	}

	@Test
	void getAllUsersTest() throws Exception {
		when(userService.getAllUsers())
				.thenReturn(List.of(userDto));

		mvc.perform(get("/users")
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].name", is(userDto.getName())))
				.andExpect(jsonPath("$[0].email", is(userDto.getEmail())));

	}

	@Test
	void updateTest() throws Exception {
		when(userService.update(any(), anyLong()))
				.thenReturn(userDto);

		mvc.perform(patch("/users/" + userDto.getId())
						.content(mapper.writeValueAsString(userDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(userDto.getName())))
				.andExpect(jsonPath("$.email", is(userDto.getEmail())));

	}

	@Test
	void getUserByIdTest() throws Exception {
		when(userService.getUserById(anyLong()))
				.thenReturn(userDto);

		mvc.perform(get("/users/" + userDto.getId())
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(userDto.getName())))
				.andExpect(jsonPath("$.email", is(userDto.getEmail())));
	}

	@Test
	void deleteTest() throws Exception {
		mvc.perform(delete("/users/" + userDto.getId())
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
