package hu.tamas.university.repository;

import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.ParticipateInEvent;
import hu.tamas.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipateInEventRepository extends JpaRepository<ParticipateInEvent, Integer> {

	ParticipateInEvent findParticipateInEventById(int id);

	List<ParticipateInEvent> findByEventAndUser(Event event, User user);
}
