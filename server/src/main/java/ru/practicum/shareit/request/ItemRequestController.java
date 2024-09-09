package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

	private final ItemRequestService requestService;
	private static final String SHARER_USER_ID = "X-Sharer-User-Id";

	@Autowired
	public ItemRequestController(ItemRequestService requestService) {
		this.requestService = requestService;
	}

	@PostMapping
	public ItemRequestDto create(@RequestBody NewItemRequestDto dto,
								 @RequestHeader(SHARER_USER_ID) long userId) {
		ItemRequestDto request = requestService.create(dto, userId);
		log.info("Created request {}", request.toString());
		return request;
	}

	@GetMapping
	public List<ItemRequestDto> findByRequester(@RequestHeader(SHARER_USER_ID) long requesterId) {
		return requestService.findByRequester(requesterId);
	}

	@GetMapping("/all")
	public List<ItemRequestDto> findAll(@RequestHeader(SHARER_USER_ID) long requesterId) {
		return requestService.findAll(requesterId);
	}

	@GetMapping("/{requestId}")
	public ItemRequestDto findByRequestId(@PathVariable("requestId") long requestId) {
		return requestService.findByRequestId(requestId);
	}
}
