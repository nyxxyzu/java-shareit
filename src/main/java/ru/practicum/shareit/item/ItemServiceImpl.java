package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.TimestampItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemStorage;
	private final UserRepository userStorage;
	private final BookingRepository bookingStorage;
	private final CommentRepository commentStorage;


	@Autowired
	public ItemServiceImpl(ItemRepository itemStorage, UserRepository userStorage, BookingRepository bookingStorage,
						   CommentRepository commentStorage) {
		this.itemStorage = itemStorage;
		this.userStorage = userStorage;
		this.bookingStorage = bookingStorage;
		this.commentStorage = commentStorage;

	}

	@Override
	@Transactional
	public ItemDto createItem(Item item, long userId) {
		User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		item.setOwner(user);
		itemStorage.save(item);
		return ItemMapper.toItemDto(item);
	}

	@Override
	public TimestampItemDto getItemById(long itemId) {
		return itemStorage.findById(itemId)
				.map(ItemMapper::toTimestampItemDto)
				.map(item -> addTimestamps(itemId))
				.map(item -> addComments(itemId))
				.orElseThrow(() -> new NotFoundException("Предмет не найден"));
	}

	@Override
	@Transactional
	public ItemDto updateItem(Item item, long userId, long itemId) {
		Item oldItem = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден"));
		if (oldItem.getOwner().getId() != userId) {
			throw new NotFoundException("Пользователь не является владельцем вещи");
		}
		item.setId(itemId);
		return ItemMapper.toItemDto(itemStorage.save(item));
	}

	@Override
	public Collection<TimestampItemDto> getAllItemsByOwner(long userId) {
		List<Item> items = itemStorage.findByOwnerId(userId);
		return items.stream()
				.map(ItemMapper::toTimestampItemDto)
				.map(item -> addTimestamps(item.getId()))
				.map(item -> addComments(item.getId()))
				.toList();
	}

	@Override
	public Collection<ItemDto> searchForItem(String query) {
		if (query.isBlank()) {
			return Collections.emptyList();
		}
		return itemStorage.search(query).stream()
				.map(ItemMapper::toItemDto)
				.toList();
	}

	@Override
	public CommentDto createComment(RequestCommentDto dto, long itemId, long userId) {
		if (bookingStorage.findByUserAndItemId(userId, itemId).isEmpty()) {
			throw new ValidationException("Вы не можете оставить отзыв на эту вещь.");
		}
		Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден"));
		User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException(("Пользователь не найден")));
		Comment comment = ItemMapper.toComment(dto);
		comment.setItem(item);
		comment.setAuthor(user);
		comment.setCreated(LocalDateTime.now());
		return ItemMapper.toCommentDto(commentStorage.save(comment));
	}

	public TimestampItemDto addTimestamps(long itemId) {
		List<Booking> bookings = bookingStorage.findByItemId(itemId);
		TimestampItemDto itemDto = ItemMapper.toTimestampItemDto(itemStorage.findById(itemId).get());
		for (Booking booking : bookings) {
			if (booking.getStart().isBefore(LocalDateTime.now())
					&& (itemDto.getLastBooking() == null || itemDto.getLastBooking().isBefore(booking.getStart()))) {
				itemDto.setLastBooking(booking.getStart());
			}
			if (booking.getStart().isAfter(LocalDateTime.now())
					&& (itemDto.getNextBooking() == null || itemDto.getNextBooking().isAfter(booking.getStart()))) {
				itemDto.setNextBooking(booking.getStart());
			}
		}
		return itemDto;
	}

	public TimestampItemDto addComments(long itemId) {
		List<Comment> comments = commentStorage.findByItemId(itemId);
		List<CommentDto> commentDtos = comments.stream()
				.map(ItemMapper::toCommentDto)
				.toList();
		TimestampItemDto itemDto = ItemMapper.toTimestampItemDto(itemStorage.findById(itemId).get());
		itemDto.setComments(commentDtos);
		return itemDto;
	}
}
