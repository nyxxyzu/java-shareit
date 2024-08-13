package ru.practicum.shareit.item.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class ItemDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private long id;
	private String name;
	private String description;
	private boolean available;
	private ItemRequest request;
}
