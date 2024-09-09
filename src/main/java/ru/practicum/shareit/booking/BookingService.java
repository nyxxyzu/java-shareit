package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.Collection;

public interface BookingService {
	BookingDto createBooking(RequestBookingDto booking, long userId);

	BookingDto approveBooking(long userId, long bookingId, Boolean approved);

	BookingDto getBookingById(long bookingId, long userId);

	Collection<BookingDto> getBookingsByUser(long userId, String status);

	Collection<BookingDto> getBookingsForUsersItems(long userId, String status);
}
