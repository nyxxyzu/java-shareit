package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

	private final ItemRequestRepository itemRequestRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	@Autowired
	public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRepository itemRepository,
								  UserRepository userRepository) {
		this.itemRequestRepository = itemRequestRepository;
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public ItemRequestDto create(NewItemRequestDto dto, long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
		ItemRequest request = RequestMapper.toItemRequest(dto);
		request.setCreator(user);
		request.setCreated(LocalDateTime.now());
		return RequestMapper.toRequestDto(itemRequestRepository.save(request));
	}

	@Override
	public List<ItemRequestDto> findByRequester(long requesterId) {
		Map<Long, ItemRequest> requestMap = itemRequestRepository.findByCreatorId(requesterId)
				.stream()
				.collect(Collectors.toMap(ItemRequest::getId, Function.identity()));
		Map<Long, List<Item>> itemMap = itemRepository.findByRequestsIds(requestMap.keySet())
				.stream()
				.collect(Collectors.groupingBy(item -> item.getRequest().getId()));
		return requestMap.values()
				.stream()
				.map(RequestMapper::toRequestDto)
				.map(request -> addItems(request, itemMap.getOrDefault(request.getId(), Collections.emptyList())))
				.sorted(Comparator.comparing(ItemRequestDto::getCreated))
				.toList();
	}

	public ItemRequestDto addItems(ItemRequestDto dto, List<Item> replies) {
		List<ItemRequestItemDto> repliesDtos = replies.stream()
				.map(ItemMapper::toItemRequestItemDto)
				.toList();
		dto.setItems(repliesDtos);
		return dto;
	}

	@Override
	public List<ItemRequestDto> findAll(long requesterId) {
		return itemRequestRepository.findAll(requesterId)
				.stream()
				.map(RequestMapper::toRequestDto)
				.sorted(Comparator.comparing(ItemRequestDto::getCreated))
				.toList();
	}

	@Override
	public ItemRequestDto findByRequestId(long requestId) {
		ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
		List<Item> items = itemRepository.findByRequestId(requestId);
		ItemRequestDto dto = RequestMapper.toRequestDto(request);
		return addItems(dto, items);
	}
}
