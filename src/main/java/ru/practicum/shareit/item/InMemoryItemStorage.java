package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserStorage;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryItemStorage {

	private final InMemoryUserStorage userStorage;


	@Autowired
	public InMemoryItemStorage(InMemoryUserStorage userStorage) {
		this.userStorage = userStorage;

	}

	private final Map<Long, Item> items = new HashMap<>();

	private long getNextId() {
		long currentMaxId = items.keySet()
				.stream()
				.max(Long::compareTo)
				.orElse(0L);
		return ++currentMaxId;
	}

	public Item createItem(Item item, long userId) {
		User user = userStorage.getUserById(userId);
		if (user != null) {
			item.setId(getNextId());
			item.setOwner(user);
			items.put(item.getId(), item);
			return item;
		}
		throw new NotFoundException("Такого пользователя не существует.");
	}

	public Item updateItem(Item newItem, long itemId, long userId) {
		if (items.containsKey(itemId)) {
			Item oldItem = items.get(itemId);
			if (oldItem.getOwner().getId() != userId) {
				throw new NotFoundException("Пользователь не является владельцем вещи.");
			}
			if (newItem.getName() != null) {
				oldItem.setName(newItem.getName());
			}
			if (newItem.getDescription() != null) {
				oldItem.setDescription(newItem.getDescription());
			}
			if (newItem.getAvailable() != null) {
				oldItem.setAvailable(newItem.getAvailable());
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
}
