package hu.tamas.university.repository;

import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

	Invitation findInvitationById(int id);

	Optional<List<Invitation>> findByEventId(int eventId);

	List<Invitation> findByUser(User user);

	List<Invitation> findByEventAndUser(Event event, User user);

	@Modifying
	@Transactional
	int deleteByEventId(int eventId);
}
