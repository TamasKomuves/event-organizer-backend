package hu.tamas.university.repository;

import hu.tamas.university.entity.AnswersToPoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AnswersToPollRepository extends JpaRepository<AnswersToPoll, Integer> {

	List<AnswersToPoll> findAnswersToPollByPollAnswerId(int pollAnswerId);

	Optional<AnswersToPoll> findAnswersToPollByPollAnswerIdAndUserEmail(int pollAnswerId, String userEmail);

	@Modifying
	@Transactional
	int deleteByPollAnswerIdIn(List<Integer> pollAnswerIds);
}
