package ru.practicum.shareit.item.dto;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor
public class ItemMapper {

	public static ItemDto toItemDto(Item item) {
		ItemDto dto = new ItemDto();
		dto.setId(item.getId());
		dto.setName(item.getName());
		dto.setAvailable(item.getAvailable());
		dto.setDescription(item.getDescription());
		dto.setRequest(item.getRequest() != null ? item.getRequest() : null);
		return dto;
	}

	public static Item mapToItem(NewItemRequest request) {
		Item item = new Item();
		item.setName(request.getName());
		item.setDescription(request.getDescription());
		item.setAvailable(request.getAvailable());
		return item;
	}

	public static Item updateItemFields(Item item, UpdateItemRequest request) {
		if (request.hasName()) {
			item.setName(request.getName());
		}
		if (request.hasDescription()) {
			item.setDescription(request.getDescription());
		}
		if (request.hasAvailable()) {
			item.setAvailable(request.getAvailable());
		}
		return item;
	}
}
