package hu.tamas.university.repository;

import hu.tamas.university.entity.EventRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface EventRatingRepository extends JpaRepository<EventRating, Integer> {

	Optional<EventRating> findByEventIdAndRaterEmail(int eventId, String raterEmail);

	List<EventRating> findByEventIdIn(List<Integer> eventIds);

	@Modifying
	@Transactional
	void deleteByEventId(int eventId);
}
