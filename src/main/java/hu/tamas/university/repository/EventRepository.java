package hu.tamas.university.repository;

import hu.tamas.university.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

	Event findEventById(int id);

	List<Event> findAllByEventTypeType(String type);
}
