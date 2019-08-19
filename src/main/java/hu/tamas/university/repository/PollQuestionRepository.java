package hu.tamas.university.repository;

import hu.tamas.university.entity.PollQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollQuestionRepository extends JpaRepository<PollQuestion, Integer> {

	PollQuestion findPollQuestionById(int id);

	List<PollQuestion> findAllByEventId(int eventId);
}
