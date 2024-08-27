package ru.practicum.shareit.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Data
@NoArgsConstructor
public class User {

	private Long id;
	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@NotEmpty(groups = BasicInfo.class)
	@Email(groups = AdvancedInfo.class)
	private String email;
}
