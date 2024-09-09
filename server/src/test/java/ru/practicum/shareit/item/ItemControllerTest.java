package ru.practicum.shareit.item;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.TimestampItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

	@Autowired
	ObjectMapper mapper;

	@MockBean
	ItemService itemService;

	@Autowired
	private MockMvc mvc;

	private ItemDto itemDto = new ItemDto();
	private TimestampItemDto timestampItemDto = new TimestampItemDto();

	@BeforeEach
	void makeItemDto() {
		itemDto.setId(1L);
		itemDto.setName("name");
		itemDto.setDescription("desc");
		itemDto.setAvailable(true);
		itemDto.setRequest(null);
		timestampItemDto.setId(2L);
		timestampItemDto.setName("name2");
		timestampItemDto.setComments(List.of(new CommentDto(), new CommentDto()));
		timestampItemDto.setAvailable(true);
		timestampItemDto.setDescription("desc2");
		timestampItemDto.setLastBooking(LocalDateTime.of(2020, 9, 9, 15,30,1));
		timestampItemDto.setNextBooking(LocalDateTime.of(2024, 9, 9, 15,30,1));
	}

	@Test
	void createItemTest() throws Exception {
		when(itemService.createItem(any(), anyLong()))
				.thenReturn(itemDto);

		mvc.perform(post("/items")
						.header("X-Sharer-User-Id", 1)
						.content(mapper.writeValueAsString(itemDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(itemDto.getName())))
				.andExpect(jsonPath("$.description", is(itemDto.getDescription())))
				.andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
				.andExpect(jsonPath("$.request", is(itemDto.getRequest())));
	}

	@Test
	void updateItemTest() throws Exception {
		when(itemService.updateItem(any(), anyLong(), anyLong()))
				.thenReturn(itemDto);

		mvc.perform(patch("/items/" + itemDto.getId())
				.header("X-Sharer-User-Id", 1)
				.content(mapper.writeValueAsString(itemDto))
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(itemDto.getName())))
				.andExpect(jsonPath("$.description", is(itemDto.getDescription())))
				.andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
				.andExpect(jsonPath("$.request", is(itemDto.getRequest())));
	}

	@Test
	void getItemByIdTest() throws Exception {
		when(itemService.getItemById(anyLong()))
				.thenReturn(timestampItemDto);

		mvc.perform(get("/items/" + timestampItemDto.getId())
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(timestampItemDto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(timestampItemDto.getName())))
				.andExpect(jsonPath("$.description", is(timestampItemDto.getDescription())))
				.andExpect(jsonPath("$.available", is(timestampItemDto.getAvailable())))
				.andExpect(jsonPath("$.lastBooking", is(timestampItemDto.getLastBooking().toString())))
				.andExpect(jsonPath("$.nextBooking", is(timestampItemDto.getNextBooking().toString())))
				.andExpect(jsonPath("$.comments", is(notNullValue())));
	}

	@Test
	void getAllItemsByOwnerTest() throws Exception {
		when(itemService.getAllItemsByOwner(anyLong()))
				.thenReturn(List.of(timestampItemDto));

		mvc.perform(get("/items")
				.header("X-Sharer-User-Id", 1)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(timestampItemDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].name", is(timestampItemDto.getName())))
				.andExpect(jsonPath("$[0].description", is(timestampItemDto.getDescription())))
				.andExpect(jsonPath("$[0].available", is(timestampItemDto.getAvailable())))
				.andExpect(jsonPath("$[0].lastBooking", is(timestampItemDto.getLastBooking().toString())))
				.andExpect(jsonPath("$[0].nextBooking", is(timestampItemDto.getNextBooking().toString())))
				.andExpect(jsonPath("$[0].comments", is(notNullValue())));
	}

	@Test
	void searchForItemTest() throws Exception {
		when(itemService.searchForItem(anyString()))
				.thenReturn(List.of(itemDto));

		mvc.perform(get("/items/search")
						.param("text", anyString())
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].name", is(itemDto.getName())))
				.andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
				.andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
				.andExpect(jsonPath("$[0].request", is(itemDto.getRequest())));
	}

	@Test
	void createCommentTest() throws Exception {
		CommentDto commentDto = new CommentDto();
		commentDto.setId(1L);
		commentDto.setText("text");
		commentDto.setCreated(LocalDateTime.of(2024, 9, 9, 15, 30,1));
		commentDto.setItem(itemDto);

		when(itemService.createComment(any(), anyLong(), anyLong()))
				.thenReturn(commentDto);

		mvc.perform(post("/items/" + itemDto.getId() + "/comment")
						.header("X-Sharer-User-Id", 1)
						.content(mapper.writeValueAsString(commentDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
				.andExpect(jsonPath("$.text", is(commentDto.getText())))
				.andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())))
				.andExpect(jsonPath("$.item", notNullValue()));


	}

}
