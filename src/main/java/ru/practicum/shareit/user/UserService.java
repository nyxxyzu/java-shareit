package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {
	private final InMemoryUserStorage userStorage;


	@Autowired
	public UserService(InMemoryUserStorage userStorage) {
		this.userStorage = userStorage;

	}

	public User create(User user) {
		return userStorage.create(user);
	}

	public User getUserById(long id) {
		return userStorage.getUserById(id);
	}

	public User update(User user, long userId) {
		return userStorage.update(user, userId);
	}

	public Collection<User> getAllUsers() {
		return userStorage.getAllUsers();
	}

	public void deleteUser(long userId) {
		userStorage.deleteUser(userId);
	}
}
