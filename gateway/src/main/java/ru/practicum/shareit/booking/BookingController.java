package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.validationgroups.BasicInfo;

import static ru.practicum.shareit.MyValues.SHARER_USER_ID;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBooking(@Validated({BasicInfo.class}) @RequestBody RequestBookingDto booking,
												@RequestHeader(SHARER_USER_ID) long userId) {
		return bookingClient.createBooking(booking, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader(SHARER_USER_ID) long userId,
												 @PathVariable("bookingId") long bookingId,
												 @RequestParam("approved") Boolean approved) {
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable("bookingId") long bookingId) {
		return bookingClient.getBookingById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByUser(@RequestHeader(SHARER_USER_ID) long userId,
													@RequestParam(defaultValue = "all") String status) {
		return bookingClient.getBookingsByUser(userId, status);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsForUsersItems(@RequestHeader(SHARER_USER_ID) long ownerId,
														   @RequestParam(required = false, defaultValue = "all") String status) {
		return bookingClient.getBookingsForUsersItems(ownerId, status);
	}


}
