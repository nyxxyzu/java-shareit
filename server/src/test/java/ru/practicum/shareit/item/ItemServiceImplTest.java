package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.TimestampItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {

	private final ItemService itemService;
	private final UserService userService;
	private final BookingService bookingService;
	private RequestItemDto dto;
	private UserDto user;
	private ItemDto item;

	private RequestItemDto makeItemDto(String name, String description, Boolean available, Long requestId) {
		RequestItemDto dto = new RequestItemDto();
		dto.setName(name);
		dto.setDescription(description);
		dto.setAvailable(available);
		dto.setRequestId(requestId);
		return dto;
	}

	private RequestUserDto makeUserDto(String name, String email) {
		RequestUserDto dto = new RequestUserDto();
		dto.setName(name);
		dto.setEmail(email);
		return dto;
	}

	@BeforeEach
	void createObjects() {
		dto = makeItemDto("Item", "Description", true, null);
		user = userService.create(makeUserDto("name", "email@email.com"));
		item = itemService.createItem(dto, user.getId());
	}

	@Test
	void createTest() {
		assertThat(item.getId(), notNullValue());
		assertThat(item.getName(), equalTo(dto.getName()));
		assertThat(item.getDescription(), equalTo(dto.getDescription()));
		assertThat(item.getAvailable(), equalTo(dto.getAvailable()));
		assertThat(item.getRequest(), nullValue());
	}

	@Test
	void testGetById() {
		TimestampItemDto foundItem = itemService.getItemById(item.getId());
		assertThat(foundItem, notNullValue());
		assertThat(foundItem.getId(), equalTo(item.getId()));
		assertThat(foundItem.getName(), equalTo(item.getName()));
		assertThat(foundItem.getDescription(), equalTo(item.getDescription()));
		assertThat(foundItem.getAvailable(), equalTo(item.getAvailable()));

	}

	@Test
	void testUpdate() {
		ItemDto updatedItem = itemService.updateItem(makeItemDto("newname", "newdesc", false,null),
				user.getId(), item.getId());
		assertThat(updatedItem.getName(), equalTo("newname"));
		assertThat(updatedItem.getDescription(), equalTo("newdesc"));
		assertThat(updatedItem.getAvailable(), equalTo(false));
		assertThat(updatedItem.getRequest(), equalTo(item.getRequest()));

	}

	@Test
	void testGetAllItemsByOwner() {
		Collection<TimestampItemDto> items = itemService.getAllItemsByOwner(user.getId());
		assertThat(items.size(), equalTo(1));
	}

	@Test
	void testSearch() {
		Collection<ItemDto> items = itemService.searchForItem("Description");
		assertThat(items.size(), equalTo(1));
	}

	@Test
	void testCreateComment() {
		RequestCommentDto dto = new RequestCommentDto();
		dto.setText("text");
		RequestBookingDto bookingDto = new RequestBookingDto();
		bookingDto.setItemId(item.getId());
		bookingDto.setStart(LocalDateTime.of(2020,9,9,15,30));
		bookingDto.setEnd(LocalDateTime.of(2021,9,9,15,30));
		bookingService.createBooking(bookingDto, user.getId());
		CommentDto comment = itemService.createComment(dto, item.getId(), user.getId());
		assertThat(comment.getId(), notNullValue());
		assertThat(comment.getText(), equalTo(dto.getText()));
		assertThat(comment.getItem(), notNullValue());
		assertThat(comment.getCreated(), notNullValue());
		assertThat(comment.getAuthorName(), equalTo(user.getName()));
		RequestItemDto newDto = new RequestItemDto();
		newDto.setName("name3");
		newDto.setAvailable(true);
		newDto.setDescription("desc3");
		ItemDto newItem = itemService.createItem(newDto, user.getId());
		assertThrows(ValidationException.class, () -> {
			itemService.createComment(dto, newItem.getId(), user.getId());
		});


	}

	@Test
	void cannotUpdateIfNotOwnerTest() {
		RequestUserDto newUser = new RequestUserDto();
		newUser.setEmail("email2@gmail.com");
		newUser.setName("newname");
		UserDto user2 = userService.create(newUser);
		assertThrows(NotFoundException.class, () -> {
			itemService.updateItem(makeItemDto(null, "newdesc", null,null),
					user2.getId(), item.getId());
		});
	}

}
