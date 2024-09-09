package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TimestampItemDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private long id;
	private String name;
	private String description;
	private Boolean available;
	private LocalDateTime lastBooking;
	private LocalDateTime nextBooking;
	private List<CommentDto> comments;

}
