package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class RequestItemDto {

	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@NotEmpty(groups = BasicInfo.class)
	private String description;
	@NotNull(groups = BasicInfo.class)
	private Boolean available;
	private Long requestId;
}
