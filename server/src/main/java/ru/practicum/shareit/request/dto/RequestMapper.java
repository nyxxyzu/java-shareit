package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

public class RequestMapper {

	public static ItemRequestDto toRequestDto(ItemRequest request) {
		ItemRequestDto dto = new ItemRequestDto();
		dto.setId(request.getId());
		dto.setRequestingUser(request.getCreator() != null ? UserMapper.mapToUserDto(request.getCreator()) : null);
		dto.setCreated(request.getCreated());
		dto.setDescription(request.getDescription());
		return dto;
	}

	public static ItemRequest toItemRequest(NewItemRequestDto dto) {
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setDescription(dto.getDescription());
		return itemRequest;
	}


}
