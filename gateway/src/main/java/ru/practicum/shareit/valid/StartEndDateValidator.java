package ru.practicum.shareit.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.RequestBookingDto;

import java.time.LocalDateTime;

public class StartEndDateValidator implements ConstraintValidator<StartBeforeEnd, RequestBookingDto> {

	@Override
	public void initialize(StartBeforeEnd startBeforeEnd) {

	}

	@Override
	public boolean isValid(RequestBookingDto dto, ConstraintValidatorContext constraintValidatorContext) {
		LocalDateTime start = dto.getStart();
		LocalDateTime end = dto.getEnd();
		if (start == null || end == null) {
			return false;
		}
		return start.isBefore(end);
	}
}
