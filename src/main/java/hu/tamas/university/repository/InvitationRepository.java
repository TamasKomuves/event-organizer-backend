package hu.tamas.university.repository;

import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

	Invitation findInvitationById(int id);

	List<Invitation> findByEvent(Event event);

	List<Invitation> findByEventAndUser(Event event, User user);
}
