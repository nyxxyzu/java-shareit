package ru.practicum.shareit.item.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Data
public class ItemDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private long id;
	private String name;
	private String description;
	private Boolean available;
	private ItemRequestDto request;
}
