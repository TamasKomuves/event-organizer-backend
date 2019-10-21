package hu.tamas.university.repository;

import hu.tamas.university.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {

	Optional<EventType> findByType(String type);
}
