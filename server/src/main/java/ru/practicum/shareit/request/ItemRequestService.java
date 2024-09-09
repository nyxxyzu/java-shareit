package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {

	ItemRequestDto create(NewItemRequestDto dto, long userId);

	List<ItemRequestDto> findByRequester(long requesterId);

	List<ItemRequestDto> findAll(long requesterId);

	ItemRequestDto findByRequestId(long requestId);
}
