package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {

	private final BookingService bookingService;
	private final UserService userService;
	private final ItemService itemService;
	private BookingDto booking;
	private UserDto user;
	private ItemDto item;


	private RequestBookingDto makeBookingDto(LocalDateTime start, LocalDateTime end, Long itemId) {
		RequestBookingDto dto = new RequestBookingDto();
		dto.setStart(start);
		dto.setEnd(end);
		dto.setItemId(itemId);
		return dto;
	}

	private RequestUserDto makeUserDto(String name, String email) {
		RequestUserDto dto = new RequestUserDto();
		dto.setName(name);
		dto.setEmail(email);
		return dto;
	}

	private RequestItemDto makeItemDto(String name, String description, Boolean available, Long requestId) {
		RequestItemDto dto = new RequestItemDto();
		dto.setName(name);
		dto.setDescription(description);
		dto.setAvailable(available);
		dto.setRequestId(requestId);
		return dto;
	}

	@BeforeEach
	void createObjects() {
		user = userService.create(makeUserDto("name", "email@email.com"));
		item = itemService.createItem(makeItemDto("itemname","itemdesc",true,null), user.getId());
		booking = bookingService.createBooking(makeBookingDto(LocalDateTime.of(2023, 9, 9,15,30),
				LocalDateTime.of(2023, 9, 9,15,30).plusMinutes(10), item.getId()), user.getId());
	}

	@Test
	void testCreate() {
		assertThat(booking.getId(), notNullValue());
		assertThat(booking.getStart(), equalTo(LocalDateTime.of(2023, 9, 9,15,30)));
		assertThat(booking.getEnd(), equalTo(LocalDateTime.of(2023, 9, 9,15,30).plusMinutes(10)));
		assertThat(booking.getItem(), equalTo(item));
		assertThat(booking.getBooker(), equalTo(user));
		assertThat(booking.getStatus(), equalTo(Status.WAITING));
	}

	@Test
	void testApprove() {
		BookingDto approvedBooking = bookingService.approveBooking(user.getId(), booking.getId(), true);
		assertThat(approvedBooking.getStatus(), equalTo(Status.APPROVED));
		assertThrows(ValidationException.class, () -> {
			bookingService.approveBooking(10, booking.getId(), true);
		});
		assertThrows(NotFoundException.class, () -> {
			bookingService.approveBooking(user.getId(), booking.getId(), null);
		});
	}

	@Test
	void testGetById() {
		BookingDto foundBooking = bookingService.getBookingById(booking.getId(), user.getId());
		assertThat(foundBooking, notNullValue());
		assertThat(foundBooking.getId(), equalTo(booking.getId()));
		assertThat(foundBooking.getStatus(), equalTo(booking.getStatus()));
		assertThat(foundBooking.getStart(), equalTo(booking.getStart()));
		assertThat(foundBooking.getEnd(), equalTo(booking.getEnd()));
		assertThat(foundBooking.getItem(), equalTo(booking.getItem()));
		assertThat(foundBooking.getBooker(), equalTo(booking.getBooker()));
		assertThrows(ValidationException.class, () -> {
			bookingService.getBookingById(booking.getId(), 2);
		});
	}

	@Test
	void testGetByUser() {
		Collection<BookingDto> bookings = bookingService.getBookingsByUser(user.getId(), "all");
		assertThat(bookings.size(), equalTo(1));
		Collection<BookingDto> bookingsFuture = bookingService.getBookingsByUser(user.getId(), "future");
		assertThat(bookingsFuture.size(), equalTo(0));
		Collection<BookingDto> bookingsPast = bookingService.getBookingsByUser(user.getId(), "past");
		assertThat(bookingsPast.size(), equalTo(1));
		Collection<BookingDto> bookingsCurrent = bookingService.getBookingsByUser(user.getId(), "current");
		assertThat(bookingsCurrent.size(), equalTo(0));
		Collection<BookingDto> bookingsStatus = bookingService.getBookingsByUser(user.getId(), "rejected");
		assertThat(bookingsStatus.size(), equalTo(0));
	}

	@Test
	void testGetForUsersItems() {
		Collection<BookingDto> bookings = bookingService.getBookingsForUsersItems(user.getId(), "all");
		assertThat(bookings.size(), equalTo(1));
		Collection<BookingDto> bookingsFuture = bookingService.getBookingsForUsersItems(user.getId(), "future");
		assertThat(bookingsFuture.size(), equalTo(0));
		Collection<BookingDto> bookingsPast = bookingService.getBookingsForUsersItems(user.getId(), "past");
		assertThat(bookingsPast.size(), equalTo(1));
		Collection<BookingDto> bookingsCurrent = bookingService.getBookingsForUsersItems(user.getId(), "current");
		assertThat(bookingsCurrent.size(), equalTo(0));
		Collection<BookingDto> bookingsStatus = bookingService.getBookingsForUsersItems(user.getId(), "rejected");
		assertThat(bookingsStatus.size(), equalTo(0));

	}

	@Test
	void testCantMakeStartAndEndTheSame() {
		assertThrows(ValidationException.class, () -> {
			bookingService.createBooking(makeBookingDto(LocalDateTime.of(2023, 9, 9,15,30),
					LocalDateTime.of(2023, 9, 9,15,30), item.getId()), user.getId());
		});
	}

}
