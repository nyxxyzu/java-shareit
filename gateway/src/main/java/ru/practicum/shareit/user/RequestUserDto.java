package ru.practicum.shareit.user;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class RequestUserDto {

	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@NotEmpty(groups = BasicInfo.class)
	@Email(groups = AdvancedInfo.class)
	private String email;
}
