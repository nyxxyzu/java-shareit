package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validationgroups.BasicInfo;

import java.time.LocalDateTime;

@Data
public class RequestBookingDto {

	@NotNull(groups = BasicInfo.class)
	private Long itemId;
	@NotNull(groups = BasicInfo.class)
	@FutureOrPresent(groups = BasicInfo.class)
	private LocalDateTime start;
	@NotNull(groups = BasicInfo.class)
	@FutureOrPresent(groups = BasicInfo.class)
	private LocalDateTime end;
}
