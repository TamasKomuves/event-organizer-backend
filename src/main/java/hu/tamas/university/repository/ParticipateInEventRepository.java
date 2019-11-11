package hu.tamas.university.repository;

import hu.tamas.university.entity.ParticipateInEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ParticipateInEventRepository extends JpaRepository<ParticipateInEvent, Integer> {

	ParticipateInEvent findParticipateInEventById(int id);

	Optional<List<ParticipateInEvent>> findByEventIdAndUserEmail(int eventId, String userEmail);

	@Modifying
	@Transactional
	int deleteByEventId(int eventId);

	@Modifying
	@Transactional
	int deleteByUserEmail(String userEmail);
}
