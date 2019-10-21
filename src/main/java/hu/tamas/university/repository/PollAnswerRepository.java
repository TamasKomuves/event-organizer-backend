package hu.tamas.university.repository;

import hu.tamas.university.entity.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Integer> {

	PollAnswer findPollAnswerById(int id);

	List<PollAnswer> findPollAnswersByPollQuestionId(int pollQuestionId);
}
