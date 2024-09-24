package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end);

	List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start);

	List<Booking> findByBookerIdAndEndIsAfter(Long bookerId, LocalDateTime end);

	List<Booking> findByBookerId(Long userId);

    @Query(value = " select b.* from bookings as b " +
		   " where b.booker_id = ?1 and b.status like ?2 ", nativeQuery = true)
	List<Booking> findByBookerWithStatus(Long bookerId, String status);

	@Query(value = " select b.* from bookings as b" +
			       " join items as i on b.item_id = i.id " +
			       " where i.owner_id = ?1", nativeQuery = true)
	List<Booking> findByOwnerId(long ownerId);

	@Query(value = " select b.* from bookings as b" +
			" join items as i on b.item_id = i.id " +
			" where i.owner_id = ?1 and b.end_date < ?2", nativeQuery = true)
	List<Booking> findByOwnerIdAndEndIsBefore(long ownerId, LocalDateTime end);


	@Query(value = " select b.* from bookings as b" +
			" join items as i on b.item_id = i.id " +
			" where i.owner_id = ?1 and b.end_date > ?2", nativeQuery = true)
	List<Booking> findByOwnerIdAndEndIsAfter(long ownerId, LocalDateTime end);

	@Query(value = " select b.* from bookings as b" +
			" join items as i on b.item_id = i.id " +
			" where i.owner_id = ?1 and b.start_date > ?2", nativeQuery = true)
	List<Booking> findByOwnerIdAndStartIsAfter(long ownerId, LocalDateTime start);

	@Query(value = " select b.* from bookings as b" +
			" join items as i on b.item_id = i.id " +
			" where i.owner_id = ?1 and b.status ilike ?2", nativeQuery = true)
	List<Booking> findByOwnerIdWithStatus(long ownerId, String status);

	@Query(value = "select b.* from bookings as b" +
	               " join items as i on b.item_id = i.id" +
	               " where i.id in ?1", nativeQuery = true)
	List<Booking> findByItemsIds(Set<Long> itemIds);

	@Query(value = "select b.* from bookings as b" +
			" join items as i on b.item_id = i.id" +
			" where i.id = ?1", nativeQuery = true)
	List<Booking> findByItemId(long itemId);

	@Query(value = "select b.* from bookings as b" +
	               " join items as i on b.item_id = i.id" +
	               " where b.booker_id = ?1 and i.id = ?2 and b.end_date <= current_timestamp", nativeQuery = true)
	List<Booking> findByUserAndItemId(long userId, long itemId);
}
