package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoJsonTest {

	private final JacksonTester<BookingDto> json;

	@Test
	void testBookingDto() throws Exception {

		BookingDto bookingDto = new BookingDto();
		bookingDto.setId(1L);
		bookingDto.setStatus(Status.WAITING);
		bookingDto.setStart(LocalDateTime.of(2020,9,9,15,30,1));
		bookingDto.setEnd(LocalDateTime.of(2021,9,9,15,30,1));

		JsonContent<BookingDto> result = json.write(bookingDto);

		assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
		assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.WAITING.name());
		assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStart().toString());
		assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEnd().toString());


	}
}
