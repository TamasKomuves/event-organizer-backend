package hu.tamas.university.repository;

import hu.tamas.university.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

	Event findEventById(int id);

	List<Event> findAllByEventTypeType(String type);

	@Modifying
	@Transactional
	@Query("UPDATE Event SET organizer_email = NULL where organizer_email = :organizerEmail")
	int updateByOrganizerEmailOrganizerEmailToNull(@Param("organizerEmail") String organizerEmail);

	@Query("SELECT id FROM Event WHERE organizer_email = :organizerEmail")
	List<Integer> findIdByOrganizerEmail(String organizerEmail);
}
