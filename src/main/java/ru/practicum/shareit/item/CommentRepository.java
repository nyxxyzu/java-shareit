package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query(value = "select c.* from comments as c" +
			" join items as i on c.item_id = i.id" +
			" where i.id = ?1", nativeQuery = true)
	List<Comment> findByItemId(long itemId);

}
