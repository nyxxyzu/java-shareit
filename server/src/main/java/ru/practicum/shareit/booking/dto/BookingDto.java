package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BookingDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	private LocalDateTime start;
	private LocalDateTime end;
	private ItemDto item;
	private UserDto booker;
	private Status status;

}