package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

	List<ItemRequest> findByCreatorId(long requesterId);

	@Query(value = "select r from ItemRequest r " +
			"where r.creator.id != ?1")
	List<ItemRequest> findAll(long requesterId);
}
