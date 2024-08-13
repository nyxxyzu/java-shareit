package ru.practicum.shareit.booking;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Booking {

	private Long id;
	private LocalDateTime bookingStart;
	@NotEmpty
	private LocalDateTime bookingEnd;
	@NotEmpty
	private Item item;
	@NotEmpty
	private User booker;
	@NotEmpty
	private Status status;

}
