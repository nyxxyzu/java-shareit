package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validationgroups.BasicInfo;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(groups = BasicInfo.class)
	@Size(max = 50)
	private String name;
	@NotEmpty(groups = BasicInfo.class)
	@Size(max = 200)
	private String description;
	@Column(name = "is_available")
	@NotNull(groups = BasicInfo.class)
	private Boolean available;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;
	@ToString.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_id")
	private ItemRequest request;

}
