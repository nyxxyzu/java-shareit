package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	private String text;
	private ItemDto item;
	private String authorName;
	private LocalDateTime created;
}
