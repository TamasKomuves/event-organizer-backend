package hu.tamas.university.repository;

import hu.tamas.university.entity.ParticipateInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipateInEventRepository extends JpaRepository<ParticipateInEvent, Integer> {

	ParticipateInEvent findParticipateInEventById(int id);

	Optional<List<ParticipateInEvent>> findByEventIdAndUserEmail(int eventId, String userEmail);
}
