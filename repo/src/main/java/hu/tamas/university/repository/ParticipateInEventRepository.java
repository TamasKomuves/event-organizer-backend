package hu.tamas.university.repository;

import hu.tamas.university.entity.ParticipateInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateInEventRepository extends JpaRepository<ParticipateInEvent, Long> {

	ParticipateInEvent findParticipateInEventById(int id);
}
