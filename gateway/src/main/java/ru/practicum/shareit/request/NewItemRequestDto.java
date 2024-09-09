package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class NewItemRequestDto {

	@NotEmpty(groups =  BasicInfo.class)
	private String description;
}
