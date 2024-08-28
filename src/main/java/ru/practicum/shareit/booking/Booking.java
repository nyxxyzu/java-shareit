package ru.practicum.shareit.booking;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validationgroups.BasicInfo;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(groups = BasicInfo.class)
	@Column(name = "start_date")
	@FutureOrPresent
	private LocalDateTime start;
	@NotEmpty(groups = BasicInfo.class)
	@FutureOrPresent
	@Column(name = "end_date")
	private LocalDateTime end;
	@NotEmpty(groups = BasicInfo.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	@NotEmpty(groups = BasicInfo.class)
	@ManyToOne(fetch = FetchType.LAZY)
	private User booker;
	@NotEmpty(groups = BasicInfo.class)
	@Enumerated(value = EnumType.STRING)
	private Status status;

}
