package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

import static ru.practicum.shareit.MyValues.SHARER_USER_ID;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemClient itemClient;

	@PostMapping
	public ResponseEntity<Object> createItem(@Validated({BasicInfo.class, AdvancedInfo.class}) @RequestBody RequestItemDto dto,
											 @RequestHeader(SHARER_USER_ID) long userId) {
		return itemClient.createItem(dto, userId);
	}

	@PatchMapping("/{itemId}")
	public ResponseEntity<Object> updateItem(@Validated({AdvancedInfo.class}) @RequestBody RequestItemDto dto, @PathVariable("itemId") long itemId,
											 @RequestHeader(SHARER_USER_ID) long userId) {
		return itemClient.updateItem(dto, itemId, userId);
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<Object> getItemById(@PathVariable("itemId") long itemId) {
		return itemClient.getItemById(itemId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllItemsByOwner(@RequestHeader(SHARER_USER_ID) long userId) {
		return itemClient.getAllItemsByOwner(userId);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchForItem(@RequestParam("text") String query) {
		return itemClient.searchForItem(query);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> createComment(@PathVariable("itemId") long itemId,
									@Validated({BasicInfo.class, AdvancedInfo.class}) @RequestBody RequestCommentDto dto, @RequestHeader(SHARER_USER_ID) long userId) {
		return itemClient.createComment(itemId, dto, userId);
	}
}
