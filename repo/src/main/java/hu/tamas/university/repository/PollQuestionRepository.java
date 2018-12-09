package hu.tamas.university.repository;

import hu.tamas.university.entity.PollQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollQuestionRepository extends JpaRepository<PollQuestion, Long> {

	PollQuestion findPollQuestionById(int id);
}
