package ru.practicum.shareit.request;


import jakarta.validation.constraints.NotEmpty;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class ItemRequest {

	private Long id;
	@NotEmpty
	private String description;
	@NotEmpty
	private User requestingUser;
	private LocalDateTime created;
}
