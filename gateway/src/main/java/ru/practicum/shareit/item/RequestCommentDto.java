package ru.practicum.shareit.item;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class RequestCommentDto {

	@NotEmpty(groups = BasicInfo.class)
	private String text;
}
