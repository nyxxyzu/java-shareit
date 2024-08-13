package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage {

	private final Map<Long, User> users = new HashMap<>();
	private final Set<String> emails = new HashSet<>();

	private long getNextId() {
		long currentMaxId = users.keySet()
				.stream()
				.max(Long::compareTo)
				.orElse(0L);
		return ++currentMaxId;
	}

	public Collection<User> getAllUsers() {
		return users.values();
	}

	public User create(User user) {
		user.setId(getNextId());
		if (emails.contains(user.getEmail())) {
			throw new ValidationException("Такой e-mail уже используется");
		}
		users.put(user.getId(), user);
		emails.add(user.getEmail());
		return user;
	}

	public User update(User newUser, long userId) {
		if (users.containsKey(userId)) {
			User oldUser = users.get(userId);
			if (emails.contains(newUser.getEmail())) {
				throw new ValidationException("Такой e-mail уже используется");
			} else if (newUser.getEmail() != null) {
				emails.remove(oldUser.getEmail());
				emails.add(newUser.getEmail());
				oldUser.setEmail(newUser.getEmail());
			}
			if (newUser.getName() != null) {
				oldUser.setName(newUser.getName());
			}
			return oldUser;
		}
		throw new NotFoundException("Пользователь с id = " + userId + " не найден");

	}

	public User getUserById(long userId) {
		return users.get(userId);
	}

	public void deleteUser(long userId) {
		users.remove(userId);
	}
}
