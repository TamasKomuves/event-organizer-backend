package hu.tamas.university.repository;

import hu.tamas.university.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Post findPostById(int id);

	List<Post> findAllByEventId(int eventId);

	@Query("SELECT id FROM Post WHERE event_id = :eventId")
	Optional<List<Integer>> findIdByEventId(@Param("eventId") int eventId);

	@Modifying
	@Transactional
	int deleteByEventId(int eventId);

	@Modifying
	@Transactional
	@Query("UPDATE Post SET poster_email = NULL WHERE poster_email = :userEmail")
	int updateByUserEmail(@Param("userEmail") String userEmail);
}
