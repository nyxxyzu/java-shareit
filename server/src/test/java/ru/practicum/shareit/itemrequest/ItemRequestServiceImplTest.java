package ru.practicum.shareit.itemrequest;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {

	private final ItemRequestService requestService;
	private final UserService userService;
	private final ItemService itemService;
	private ItemRequestDto request;
	private UserDto user;
	private ItemDto item;

	private NewItemRequestDto makeItemRequestDto(String description) {
		NewItemRequestDto dto = new NewItemRequestDto();
		dto.setDescription(description);
		return dto;
	}

	private RequestUserDto makeUserDto(String name, String email) {
		RequestUserDto dto = new RequestUserDto();
		dto.setName(name);
		dto.setEmail(email);
		return dto;
	}

	private RequestItemDto makeItemDto(String name, String description, Boolean available, Long requestId) {
		RequestItemDto dto = new RequestItemDto();
		dto.setName(name);
		dto.setDescription(description);
		dto.setAvailable(available);
		dto.setRequestId(requestId);
		return dto;
	}

	@BeforeEach
	void createObjects() {
		user = userService.create(makeUserDto("name", "email@email.com"));
		request = requestService.create(makeItemRequestDto("desc"), user.getId());
		item = itemService.createItem(makeItemDto("itemname","itemdesc",true, request.getId()), user.getId());
	}

	@Test
	void testCreate() {
		assertThat(request.getId(), notNullValue());
		assertThat(request.getCreated(), notNullValue());
		assertThat(request.getRequestingUser(), equalTo(user));
		assertThat(request.getDescription(), equalTo("desc"));
	}

	@Test
	void testFindByRequester() {
		List<ItemRequestDto> requests = requestService.findByRequester(user.getId());
		assertThat(requests.size(), equalTo(1));
	}

	@Test
	void testFindAll() {
		List<ItemRequestDto> requests = requestService.findAll(user.getId());
		assertThat(requests.size(), equalTo(0));
	}

	@Test
	void testFindById() {
		ItemRequestDto itemRequest = requestService.findByRequestId(request.getId());
		assertThat(itemRequest, notNullValue());
		assertThat(itemRequest.getId(), equalTo(request.getId()));
		assertThat(itemRequest.getRequestingUser(), equalTo(request.getRequestingUser()));
		assertThat(itemRequest.getCreated(), equalTo(request.getCreated()));
		assertThat(itemRequest.getDescription(), equalTo(request.getDescription()));
	}
}
