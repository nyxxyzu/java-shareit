package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryItemRepository {

	private final Map<Long, Item> items = new HashMap<>();

	private long getNextId() {
		long currentMaxId = items.keySet()
				.stream()
				.max(Long::compareTo)
				.orElse(0L);
		return ++currentMaxId;
	}

	public Item createItem(Item item) {
			item.setId(getNextId());
			items.put(item.getId(), item);
			return item;
	}

	public Item updateItem(long itemId, long userId) {
		if (items.containsKey(itemId)) {
			Item oldItem = items.get(itemId);
			if (oldItem.getOwner().getId() != userId) {
				throw new NotFoundException("Пользователь не является владельцем вещи.");
			}
			return oldItem;
		}
		throw new NotFoundException("Предмет с id = " + itemId + " не найден");
	}

	public Item getItemById(long itemId) {
		return items.get(itemId);
	}

	public Collection<Item> getAllItems() {
		return items.values();
	}

	public Collection<Item> getAllItemsByOwner(long userId) {
		return items.values().stream()
				.filter(item -> item.getOwner().getId() == userId)
				.toList();
	}
}
