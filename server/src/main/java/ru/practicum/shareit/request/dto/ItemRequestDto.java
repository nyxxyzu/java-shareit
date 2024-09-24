package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.item.dto.ItemRequestItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	private String description;
	private UserDto requestingUser;
	private LocalDateTime created;
	private List<ItemRequestItemDto> items;
}
