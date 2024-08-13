package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Collections;

@Service
public class ItemServiceImpl implements ItemService {

	private final InMemoryItemStorage itemStorage;


	@Autowired
	public ItemServiceImpl(InMemoryItemStorage itemStorage) {
		this.itemStorage = itemStorage;

	}

	@Override
	public ItemDto createItem(NewItemRequest request, long userId) {
		Item item = ItemMapper.mapToItem(request);
		item = itemStorage.createItem(item, userId);
		return ItemMapper.toItemDto(item);
	}

	@Override
	public ItemDto getItemById(long itemId) {
		Item item = itemStorage.getItemById(itemId);
		if (item != null) {
			return ItemMapper.toItemDto(item);
		}
		throw new NotFoundException("Предмет не найден");
	}

	@Override
	public ItemDto updateItem(UpdateItemRequest request, long userId, long itemId) {
		Item updatedItem = itemStorage.getItemById(itemId);
		if (updatedItem != null) {
			ItemMapper.updateItemFields(updatedItem, request);
			updatedItem = itemStorage.updateItem(updatedItem, itemId, userId);
			return ItemMapper.toItemDto(updatedItem);
		}
		throw new NotFoundException("Предмет не найден");
	}

	@Override
	public Collection<ItemDto> getAllItemsByOwner(long userId) {
		return itemStorage.getAllItems().stream()
				.filter(item -> item.getOwner().getId() == userId)
				.map(ItemMapper::toItemDto)
				.toList();
	}

	@Override
	public Collection<ItemDto> searchForItem(String query) {
		if (query.isBlank()) {
			return Collections.emptyList();
		}
		return itemStorage.getAllItems().stream()
				.filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()) ||
						item.getDescription().toLowerCase().contains(query.toLowerCase()))
				.filter(Item::getAvailable)
				.map(ItemMapper::toItemDto)
				.toList();
	}
}
