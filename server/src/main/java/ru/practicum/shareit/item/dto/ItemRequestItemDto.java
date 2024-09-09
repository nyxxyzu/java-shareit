package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemRequestItemDto {

	private Long id;
	private String name;
	private Long ownerId;

}
