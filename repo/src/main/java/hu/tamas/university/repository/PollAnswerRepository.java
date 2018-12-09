package hu.tamas.university.repository;

import hu.tamas.university.entity.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {

	PollAnswer findPollAnswerById(int id);
}
