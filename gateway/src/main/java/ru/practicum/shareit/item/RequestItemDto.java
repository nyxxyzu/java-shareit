package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class RequestItemDto {

	@Size(max = 255, groups = AdvancedInfo.class)
	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@Size(max = 512, groups = AdvancedInfo.class)
	@NotEmpty(groups = BasicInfo.class)
	private String description;
	@NotNull(groups = BasicInfo.class)
	private Boolean available;
	private Long requestId;
}
