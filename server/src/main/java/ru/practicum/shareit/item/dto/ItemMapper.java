package ru.practicum.shareit.item.dto;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestMapper;

@NoArgsConstructor
public class ItemMapper {

	public static ItemDto toItemDto(Item item) {
		ItemDto dto = new ItemDto();
		dto.setId(item.getId());
		dto.setName(item.getName());
		dto.setAvailable(item.getAvailable());
		dto.setDescription(item.getDescription());
		dto.setRequest(item.getRequest() != null ? RequestMapper.toRequestDto(item.getRequest()) : null);
		return dto;
	}

	public static Item toItem(RequestItemDto dto) {
		Item item = new Item();
		item.setDescription(dto.getDescription());
		item.setName(dto.getName());
		item.setAvailable(dto.getAvailable());
		return item;
	}

	public static ItemRequestItemDto toItemRequestItemDto(Item item) {
		ItemRequestItemDto dto = new ItemRequestItemDto();
		dto.setId(item.getId());
		dto.setOwnerId(item.getOwner().getId());
		dto.setName(item.getName());
		return dto;
	}

	public static TimestampItemDto toTimestampItemDto(Item item) {
		TimestampItemDto dto = new TimestampItemDto();
		dto.setId(item.getId());
		dto.setName(item.getName());
		dto.setAvailable(item.getAvailable());
		dto.setDescription(item.getDescription());
		return dto;
	}

	public static CommentDto toCommentDto(Comment comment) {
		CommentDto dto = new CommentDto();
		dto.setId(comment.getId());
		dto.setText(comment.getText());
		dto.setAuthorName(comment.getAuthor().getName());
		dto.setItem(comment.getItem() != null ? ItemMapper.toItemDto(comment.getItem()) : null);
		dto.setCreated(comment.getCreated());
		return dto;
	}

	public static Comment toComment(RequestCommentDto commentDto) {
		Comment comment = new Comment();
		comment.setText(commentDto.getText());
		return comment;
	}
}
