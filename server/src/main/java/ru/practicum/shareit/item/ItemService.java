package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.TimestampItemDto;

import java.util.Collection;

public interface ItemService {
	ItemDto createItem(RequestItemDto dto, long userId);

	TimestampItemDto getItemById(long itemId);

	ItemDto updateItem(RequestItemDto dto, long userId, long itemId);

	Collection<TimestampItemDto> getAllItemsByOwner(long userId);

	Collection<ItemDto> searchForItem(String query);

	CommentDto createComment(RequestCommentDto dto, long itemId, long userId);
}
