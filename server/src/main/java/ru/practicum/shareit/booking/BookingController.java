package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

	private final BookingService bookingService;
	private static final String SHARER_USER_ID = "X-Sharer-User-Id";

	@Autowired
	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public BookingDto createBooking(@RequestBody RequestBookingDto booking,
									@RequestHeader(SHARER_USER_ID) long userId) {
		BookingDto createdBooking = bookingService.createBooking(booking, userId);
		log.info("Created booking {}", createdBooking.toString());
		return createdBooking;
	}

	@PatchMapping("/{bookingId}")
	public BookingDto approveBooking(@RequestHeader(SHARER_USER_ID) long userId,
									 @PathVariable("bookingId") long bookingId,
									 @RequestParam("approved") Boolean approved) {
		BookingDto approvedBooking = bookingService.approveBooking(userId, bookingId, approved);
		log.info("Booking " + bookingId + " status have been changed to " + approvedBooking.getStatus().toString());
		return approvedBooking;
	}

	@GetMapping("/{bookingId}")
	public BookingDto getBookingById(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable("bookingId") long bookingId) {
		return bookingService.getBookingById(bookingId, userId);
	}

	@GetMapping
	public Collection<BookingDto> getBookingsByUser(@RequestHeader(SHARER_USER_ID) long userId,
													@RequestParam(defaultValue = "all") String status) {
		return bookingService.getBookingsByUser(userId, status);
	}

	@GetMapping("/owner")
	public Collection<BookingDto> getBookingsForUsersItems(@RequestHeader(SHARER_USER_ID) long ownerId,
														   @RequestParam(required = false, defaultValue = "all") String status) {
		return bookingService.getBookingsForUsersItems(ownerId, status);
	}
}
