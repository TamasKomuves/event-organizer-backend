package hu.tamas.university.repository;

import hu.tamas.university.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {

	EventType findEventTypeByType(String type);
}
