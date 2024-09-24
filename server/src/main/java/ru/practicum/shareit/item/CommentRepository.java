package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query(value = "select c.* from comments as c" +
			" join items as i on c.item_id = i.id" +
			" where i.id in ?1", nativeQuery = true)
	List<Comment> findByItemsIds(Set<Long> itemId);

	@Query(value = "select c.* from comments as c" +
			" join items as i on c.item_id = i.id" +
			" where i.id = ?1", nativeQuery = true)
	List<Comment> findByItemId(long itemId);

}
