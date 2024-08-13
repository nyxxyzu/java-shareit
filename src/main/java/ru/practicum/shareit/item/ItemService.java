package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {
	ItemDto createItem(NewItemRequest request, long userId);

	ItemDto getItemById(long itemId);

	ItemDto updateItem(UpdateItemRequest request, long userId, long itemId);

	Collection<ItemDto> getAllItemsByOwner(long userId);

	Collection<ItemDto> searchForItem(String query);
}
