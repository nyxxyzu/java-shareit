package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.TimestampItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

	private final ItemService itemService;
	private static final String SHARER_USER_ID = "X-Sharer-User-Id";

	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@PostMapping
	public ItemDto createItem(@RequestBody RequestItemDto dto, @RequestHeader(SHARER_USER_ID) long userId) {
		ItemDto createdItem = itemService.createItem(dto, userId);
		log.info("Created item {}", createdItem.toString());
		return createdItem;
	}

	@PatchMapping("/{itemId}")
	public ItemDto updateItem(@RequestBody RequestItemDto dto, @PathVariable("itemId") long itemId,
							  @RequestHeader(SHARER_USER_ID) long userId) {
		ItemDto updatedItem = itemService.updateItem(dto, userId, itemId);
		log.info("Updated item {}", updatedItem.toString());
		return updatedItem;
	}

	@GetMapping("/{itemId}")
	public TimestampItemDto getItemById(@PathVariable("itemId") long itemId) {
		return itemService.getItemById(itemId);
	}

	@GetMapping
	public Collection<TimestampItemDto> getAllItemsByOwner(@RequestHeader(SHARER_USER_ID) long userId) {
		return itemService.getAllItemsByOwner(userId);
	}

	@GetMapping("/search")
	public Collection<ItemDto> searchForItem(@RequestParam("text") String query) {
		return itemService.searchForItem(query);
	}

	@PostMapping("/{itemId}/comment")
	public CommentDto createComment(@PathVariable("itemId") long itemId,
									@RequestBody RequestCommentDto dto, @RequestHeader(SHARER_USER_ID) long userId) {
		CommentDto createdComment = itemService.createComment(dto, itemId, userId);
		log.info("Created comment {}", createdComment.toString());
		return createdComment;
	}

}
