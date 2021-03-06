package hu.tamas.university.repository;

import hu.tamas.university.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

	Invitation findInvitationById(int id);

	Optional<List<Invitation>> findByEventId(int eventId);

	Optional<List<Invitation>> findByEventIdAndUserEmailAndDecisionDateIsNull(int eventId, String userEmail);

	Long countByUserEmailAndIsAlreadySeen(String userEmail, int IsAlreadySeen);

	@Modifying
	@Transactional
	int deleteByEventId(int eventId);

	@Modifying
	@Transactional
	int deleteByUserEmail(String userEmail);

	@Modifying
	@Transactional
	@Query("UPDATE Invitation SET is_already_seen = 1 WHERE user_email = :userEmail AND is_already_seen = 0")
	int updateAllToAlreadySeenByUserEmail(String userEmail);
}
