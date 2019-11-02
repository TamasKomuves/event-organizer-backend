package hu.tamas.university.repository;

import hu.tamas.university.entity.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Integer> {

	PollAnswer findPollAnswerById(int id);

	List<PollAnswer> findPollAnswersByPollQuestionId(int pollQuestionId);

	@Query("SELECT id FROM PollAnswer WHERE poll_question_id IN (:pollQuestionIds)")
	Optional<List<Integer>> findIdByPollQuestionIds(List<Integer> pollQuestionIds);

	@Modifying
	@Transactional
	int deleteByPollQuestionIdIn(List<Integer> pollQuestionIds);
}
