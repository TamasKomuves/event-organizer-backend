package hu.tamas.university.repository;

import hu.tamas.university.entity.AnswersToPoll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersToPollRepository extends JpaRepository<AnswersToPoll, Integer> {

	AnswersToPoll findAnswersToPollById(int id);

	List<AnswersToPoll> findAnswersToPollByPollAnswerId(int pollAnswerId);

	AnswersToPoll findAnswersToPollByPollAnswerIdAndUserEmail(int pollAnswerId, String userEmail);
}
