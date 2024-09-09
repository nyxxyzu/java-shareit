package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

	@Autowired
	ObjectMapper mapper;

	@MockBean
	BookingService bookingService;

	@Autowired
	private MockMvc mvc;

	private BookingDto bookingDto = new BookingDto();

	@BeforeEach
	void makeBookingDto() {
		bookingDto.setId(1L);
		bookingDto.setStatus(Status.WAITING);
		bookingDto.setItem(new ItemDto());
		bookingDto.setStart(LocalDateTime.of(2024,9,9,15,30,1));
		bookingDto.setEnd(LocalDateTime.of(2024,10,9,15,30,1));
		bookingDto.setBooker(new UserDto());
	}

	@Test
	void createBookingTest() throws Exception {
		when(bookingService.createBooking(any(), anyLong()))
				.thenReturn(bookingDto);

		mvc.perform(post("/bookings")
						.header("X-Sharer-User-Id", 1)
						.content(mapper.writeValueAsString(bookingDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
				.andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())))
				.andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
				.andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
				.andExpect(jsonPath("$.booker", notNullValue()))
				.andExpect(jsonPath("$.item", notNullValue()));
	}

	@Test
	void approveBookingTest() throws Exception {
		when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
				.thenReturn(bookingDto);

		mvc.perform(patch("/bookings/" + bookingDto.getId())
						.header("X-Sharer-User-Id", 1)
						.param("approved", "true")
						.content(mapper.writeValueAsString(bookingDto))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
				.andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())))
				.andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
				.andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
				.andExpect(jsonPath("$.booker", notNullValue()))
				.andExpect(jsonPath("$.item", notNullValue()));

	}

	@Test
	void getBookingByIdTest() throws Exception {
		when(bookingService.getBookingById(anyLong(), anyLong()))
				.thenReturn(bookingDto);

		mvc.perform(get("/bookings/" + bookingDto.getId())
						.header("X-Sharer-User-Id", 1)
						.param("state", "all")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
				.andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())))
				.andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
				.andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
				.andExpect(jsonPath("$.booker", notNullValue()))
				.andExpect(jsonPath("$.item", notNullValue()));
	}

	@Test
	void getBookingsByUserTest() throws Exception {
		when(bookingService.getBookingsByUser(anyLong(), anyString()))
				.thenReturn(List.of(bookingDto));

		mvc.perform(get("/bookings")
				.header("X-Sharer-User-Id", 1)
				.param("state", "all")
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().name())))
				.andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
				.andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
				.andExpect(jsonPath("$[0].booker", notNullValue()))
				.andExpect(jsonPath("$[0].item", notNullValue()));
	}

	@Test
	void getBookingsForUsersItemsTest() throws Exception {
		when(bookingService.getBookingsForUsersItems(anyLong(), anyString()))
				.thenReturn(List.of(bookingDto));

		mvc.perform(get("/bookings/owner")
						.header("X-Sharer-User-Id", 1)
						.param("state", "all")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
				.andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().name())))
				.andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
				.andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
				.andExpect(jsonPath("$[0].booker", notNullValue()))
				.andExpect(jsonPath("$[0].item", notNullValue()));
	}
}
