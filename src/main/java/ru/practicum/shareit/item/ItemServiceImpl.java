package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
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
		Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден"));
		List<Comment> comments = commentStorage.findByItemId(itemId);
		TimestampItemDto itemDto = ItemMapper.toTimestampItemDto(item);
		return addComments(itemDto, comments);
	}

	@Override
	@Transactional
	public ItemDto updateItem(Item item, long userId, long itemId) {
		Item oldItem = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден"));
		if (oldItem.getOwner().getId() != userId) {
			throw new NotFoundException("Пользователь не является владельцем вещи");
		}
		if (item.getAvailable() != null) {
			oldItem.setAvailable(item.getAvailable());
		}
		if (item.getName() != null) {
			oldItem.setName(item.getName());
		}
		if (item.getDescription() != null) {
			oldItem.setDescription(item.getDescription());
		}
		return ItemMapper.toItemDto(itemStorage.save(oldItem));
	}

	@Override
	public Collection<TimestampItemDto> getAllItemsByOwner(long userId) {
		Map<Long, Item> itemMap = itemStorage.findByOwnerId(userId)
				.stream()
				.collect(Collectors.toMap(Item::getId, Function.identity()));
		Map<Long, List<Booking>> bookingMap = bookingStorage.findByItemsIds(itemMap.keySet())
				.stream()
				.collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
		Map<Long, List<Comment>> commentMap = commentStorage.findByItemsIds(itemMap.keySet())
				.stream()
				.collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
		return itemMap.values()
				.stream()
				.map(ItemMapper::toTimestampItemDto)
				.map(item -> addTimestamps(item, bookingMap.getOrDefault(item.getId(), Collections.emptyList())))
				.map(item -> addComments(item, commentMap.getOrDefault(item.getId(), Collections.emptyList())))
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

	@Transactional
	@Override
	public CommentDto createComment(RequestCommentDto dto, long itemId, long userId) {
		Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден"));
		User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException(("Пользователь не найден")));
		if (bookingStorage.findByUserAndItemId(userId, itemId).isEmpty()) {
			throw new ValidationException("Вы не можете оставить отзыв на эту вещь.");
		}
		Comment comment = ItemMapper.toComment(dto);
		comment.setItem(item);
		comment.setAuthor(user);
		comment.setCreated(LocalDateTime.now());
		return ItemMapper.toCommentDto(commentStorage.save(comment));
	}

	public TimestampItemDto addTimestamps(TimestampItemDto itemDto, List<Booking> bookings) {
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

	public TimestampItemDto addComments(TimestampItemDto itemDto, List<Comment> comments) {
		List<CommentDto> commentDtos = comments.stream()
				.map(ItemMapper::toCommentDto)
				.toList();
		itemDto.setComments(commentDtos);
		return itemDto;
	}
}
