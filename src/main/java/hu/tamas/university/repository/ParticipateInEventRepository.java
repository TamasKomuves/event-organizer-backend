package hu.tamas.university.repository;

import hu.tamas.university.entity.ParticipateInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateInEventRepository extends JpaRepository<ParticipateInEvent, Integer> {

	ParticipateInEvent findParticipateInEventById(int id);
}
