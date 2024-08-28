package ru.practicum.shareit.user;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.validationgroups.AdvancedInfo;
import ru.practicum.shareit.validationgroups.BasicInfo;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@NotEmpty(groups = BasicInfo.class)
	@Email(groups = AdvancedInfo.class)
	private String email;
}
