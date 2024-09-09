package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
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
		if (userRepository.findById(userId).isEmpty()) {
			throw new ValidationException("Пользователь не найден");
		}
		if (oldBooking.getItem().getOwner().getId() != userId) {
			throw new ValidationException("Пользователь не является владельцем вещи.");
		}
		if (approved == null) {
			throw new NotFoundException("Не указана опция для подтверждения бронирования");
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
		if (status.equalsIgnoreCase(State.ALL.name())) {
			bookings = bookingRepository.findByBookerId(userId);
		}
		if (status.equalsIgnoreCase(State.FUTURE.name())) {
			bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase(State.PAST.name())) {
			bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase(State.CURRENT.name())) {
			bookings = bookingRepository.findByBookerIdAndEndIsAfter(userId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase(State.WAITING.name()) || status.equalsIgnoreCase(State.REJECTED.name())) {
			bookings = bookingRepository.findByBookerWithStatus(userId, status);
		}
		return bookings.stream()
				.map(BookingMapper::toBookingDto)
				.sorted(Comparator.comparing(BookingDto::getStart))
				.toList();

	}

	@Override
	public Collection<BookingDto> getBookingsForUsersItems(long ownerId, String status) {
		if (userRepository.findById(ownerId).isEmpty()) {
			throw new NotFoundException("Пользователь не найден");
		}
		List<Booking> bookings = new ArrayList<>();
		if (status.equalsIgnoreCase(State.ALL.name())) {
			bookings = bookingRepository.findByOwnerId(ownerId);
		}
		if (status.equalsIgnoreCase(State.FUTURE.name())) {
			bookings = bookingRepository.findByOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase(State.PAST.name())) {
			bookings = bookingRepository.findByOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase(State.CURRENT.name())) {
			bookings = bookingRepository.findByOwnerIdAndEndIsAfter(ownerId, LocalDateTime.now());
		}
		if (status.equalsIgnoreCase(State.WAITING.name()) || status.equalsIgnoreCase(State.REJECTED.name())) {
			bookings = bookingRepository.findByOwnerIdWithStatus(ownerId, status);
		}
		return bookings.stream()
				.map(BookingMapper::toBookingDto)
				.sorted(Comparator.comparing(BookingDto::getStart))
				.toList();

	}
}
