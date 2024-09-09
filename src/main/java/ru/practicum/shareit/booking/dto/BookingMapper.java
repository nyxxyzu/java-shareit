package ru.practicum.shareit.booking.dto;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.item.dto.ItemMapper;

@NoArgsConstructor
public class BookingMapper {

	public static BookingDto toBookingDto(Booking booking) {
		BookingDto dto = new BookingDto();
		dto.setId(booking.getId());
		dto.setStart(booking.getStart());
		dto.setEnd(booking.getEnd());
		dto.setBooker(booking.getBooker() != null ? UserMapper.mapToUserDto(booking.getBooker()) : null);
		dto.setItem(booking.getItem() != null ? ItemMapper.toItemDto(booking.getItem()) : null);
		dto.setStatus(booking.getStatus());
		return dto;
	}

	public static Booking mapToBooking(RequestBookingDto dto) {
		Booking booking = new Booking();
		booking.setEnd(dto.getEnd());
		booking.setStart(dto.getStart());
		return booking;
	}

}
