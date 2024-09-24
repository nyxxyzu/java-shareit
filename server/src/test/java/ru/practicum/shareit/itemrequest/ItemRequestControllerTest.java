package ru.practicum.shareit.itemrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

	@Autowired
	ObjectMapper mapper;

	@MockBean
	ItemRequestService requestService;

	@Autowired
	private MockMvc mvc;

	private ItemRequestDto requestDto = new ItemRequestDto();

	@BeforeEach
	void makeRequestDto() {
		requestDto.setId(1L);
		requestDto.setDescription("desc");
		requestDto.setRequestingUser(new UserDto());
		requestDto.setCreated(LocalDateTime.of(2024, 9,9,15,30,1));
		requestDto.setItems(List.of(new ItemRequestItemDto(), new ItemRequestItemDto()));

	}

	@Test
	void createRequestTest() throws Exception {
		when(requestService.create(any(), anyLong()))
				.thenReturn(requestDto);

		mvc.perform(post("/requests")
						.header("X-Sharer-User-Id", 1)
						.content(mapper.writeValueAsString(requestDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
				.andExpect(jsonPath("$.description", is(requestDto.getDescription())))
				.andExpect(jsonPath("$.requestingUser", notNullValue()))
				.andExpect(jsonPath("$.created", is(requestDto.getCreated().toString())))
				.andExpect(jsonPath("$.items", notNullValue()));
	}

	@Test
	void findByRequesterTest() throws Exception {
		when(requestService.findByRequester(anyLong()))
				.thenReturn(List.of(requestDto));

		mvc.perform(get("/requests")
						.header("X-Sharer-User-Id", 1)
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
				.andExpect(jsonPath("$[0].requestingUser", notNullValue()))
				.andExpect(jsonPath("$[0].created", is(requestDto.getCreated().toString())))
				.andExpect(jsonPath("$[0].items", notNullValue()));
	}

	@Test
	void findAllTest() throws Exception {
		when(requestService.findAll(anyLong()))
				.thenReturn(List.of(requestDto));

		mvc.perform(get("/requests/all")
						.header("X-Sharer-User-Id", 1)
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
				.andExpect(jsonPath("$[0].requestingUser", notNullValue()))
				.andExpect(jsonPath("$[0].created", is(requestDto.getCreated().toString())))
				.andExpect(jsonPath("$[0].items", notNullValue()));
	}

	@Test
	void findByRequestId() throws Exception {
		when(requestService.findByRequestId(anyLong()))
				.thenReturn(requestDto);

		mvc.perform(get("/requests/" + requestDto.getId())
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
				.andExpect(jsonPath("$.description", is(requestDto.getDescription())))
				.andExpect(jsonPath("$.requestingUser", notNullValue()))
				.andExpect(jsonPath("$.created", is(requestDto.getCreated().toString())))
				.andExpect(jsonPath("$.items", notNullValue()));
	}
}
