package hu.tamas.university.repository;

import hu.tamas.university.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	Invitation findInvitationById(int id);
}
