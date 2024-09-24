package ru.practicum.shareit.item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class RequestCommentDto {

	@Size(max = 512, groups = AdvancedInfo.class)
	@NotEmpty(groups = BasicInfo.class)
	private String text;
}
