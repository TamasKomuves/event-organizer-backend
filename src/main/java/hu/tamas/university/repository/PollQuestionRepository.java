package hu.tamas.university.repository;

import hu.tamas.university.entity.PollQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PollQuestionRepository extends JpaRepository<PollQuestion, Integer> {

	PollQuestion findPollQuestionById(int id);

	List<PollQuestion> findAllByEventId(int eventId);

	@Query("SELECT id FROM PollQuestion WHERE event_id = :eventId")
	Optional<List<Integer>> findIdsByEventId(@Param("eventId") int eventId);

	@Modifying
	@Transactional
	int deleteByEventId(int eventId);
}
