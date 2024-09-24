package ru.practicum.shareit.user;

import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
public class RequestUserDto {
	@Size(max = 255, groups = AdvancedInfo.class)
	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@NotEmpty(groups = BasicInfo.class)
	@Email(groups = AdvancedInfo.class)
	@Size(max = 512, groups = AdvancedInfo.class)
	private String email;
}
