package hu.tamas.university.repository;

import hu.tamas.university.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {

	EventType findEventTypeByType(String type);
}
