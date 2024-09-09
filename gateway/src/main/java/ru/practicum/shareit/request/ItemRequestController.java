package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.validationgroups.BasicInfo;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

	private final ItemRequestClient requestClient;
	private static final String SHARER_USER_ID = "X-Sharer-User-Id";

	@PostMapping
	public ResponseEntity<Object> create(@Validated({BasicInfo.class}) @RequestBody NewItemRequestDto dto,
										 @RequestHeader(SHARER_USER_ID) long userId) {
		return requestClient.create(dto, userId);
	}

	@GetMapping
	public ResponseEntity<Object> findByRequester(@RequestHeader(SHARER_USER_ID) long requesterId) {
		return requestClient.findByRequester(requesterId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> findAll(@RequestHeader(SHARER_USER_ID) long requesterId) {
		return requestClient.findAll(requesterId);
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<Object> findByRequestId(@PathVariable("requestId") long requestId) {
		return requestClient.findByRequestId(requestId);
	}
}
