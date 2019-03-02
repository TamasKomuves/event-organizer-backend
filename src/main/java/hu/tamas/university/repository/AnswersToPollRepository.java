package hu.tamas.university.repository;

import hu.tamas.university.entity.AnswersToPoll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswersToPollRepository extends JpaRepository<AnswersToPoll, Integer> {

	AnswersToPoll findAnswersToPollById(int id);
}
