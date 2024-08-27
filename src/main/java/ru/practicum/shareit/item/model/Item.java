package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


@Data
@NoArgsConstructor
public class Item {

	private Long id;
	@NotEmpty
	@Size(max = 50)
	private String name;
	@NotEmpty
	@Size(max = 200)
	private String description;
	@NotEmpty
	private Boolean available;
	private User owner;
	private ItemRequest request;

}
