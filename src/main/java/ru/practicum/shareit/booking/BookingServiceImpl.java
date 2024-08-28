package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	@Autowired
	public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userStorage) {
		this.bookingRepository = bookingRepository;
		this.itemRepository = itemRepository;
		this.userRepository = userStorage;
	}

	@Override
	@Transactional
	public BookingDto createBooking(RequestBookingDto booking, long userId) {
		if (booking.getStart().equals(booking.getEnd())) {
			throw new ValidationException("Начало и конец бронирования не могут быть равны");
		}
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new NotFoundException("Предмет не найден"));
		if (!item.getAvailable()) {
			throw new ValidationException("Предмет не доступен.");
		}
		Booking newBooking = BookingMapper.mapToBooking(booking);
		newBooking.setStatus(Status.WAITING);
		newBooking.setBooker(user);
		newBooking.setItem(item);
		return BookingMapper.toBookingDto(bookingRepository.save(newBooking));
	}

	@Override
	@Transactional
	public BookingDto approveBooking(long userId, long bookingId, Boolean approved) {
		Booking oldBooking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

		if (oldBooking.getItem().getOwner().getId() != userId) {
			throw new ValidationException("Пользователь не является владельцем вещи.");
		}
		if (approved) {
			oldBooking.setStatus(Status.APPROVED);
		} else {
			oldBooking.setStatus(Status.REJECTED);
		}
		return BookingMapper.toBookingDto(bookingRepository.save(oldBooking));
	}

	@Override
	public BookingDto getBookingById(long bookingId, long userId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
		if (booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId) {
			return BookingMapper.toBookingDto(booking);
		} else {
			throw new ValidationException("Просмотреть бронирование может только бронирующий пользователь или владелец вещи");
		}
	}

	@Override
	public Collection<BookingDto> getBookingsByUser(long userId, String status) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new NotFoundException("Пользователь не найден");
		}
		List<Booking> bookings = new ArrayList<>();
		if (status.equalsIgnoreCase("all")) {
			bookings = bookingRepository.findByBookerId(userId);
		}
		if (status.equalsIgnoreCase("future")) {
			bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase("past")) {
			bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase("current")) {
			bookings = bookingRepository.findByBookerIdAndEndIsAfter(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase("waiting") || status.equalsIgnoreCase("rejected")) {
			bookings = bookingRepository.findByBookerWithStatus(userId, status);
		}
		return bookings.stream()
				.map(BookingMapper::toBookingDto)
				.sorted(Comparator.comparing(BookingDto::getStart))
				.toList();

	}

	@Override
	public Collection<BookingDto> getBookingsForUsersItems(long userId, String status) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new NotFoundException("Пользователь не найден");
		}
		List<Booking> bookings = new ArrayList<>();
		if (status.equalsIgnoreCase("all")) {
			bookings = bookingRepository.findByOwnerId(userId);
		}
		if (status.equalsIgnoreCase("future")) {
			bookings = bookingRepository.findByOwnerIdAndStartIsAfter(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase("past")) {
			bookings = bookingRepository.findByOwnerIdAndEndIsBefore(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase("current")) {
			bookings = bookingRepository.findByOwnerIdAndEndIsAfter(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase("waiting") || status.equalsIgnoreCase("rejected")) {
			bookings = bookingRepository.findByOwnerIdWithStatus(userId, status);
		}
		return bookings.stream()
				.map(BookingMapper::toBookingDto)
				.sorted(Comparator.comparing(BookingDto::getStart))
				.toList();

	}
}
