package hu.tamas.university.repository;

import hu.tamas.university.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

	ChatMessage findById(int id);

	List<ChatMessage> findBySenderEmailAndReceiverEmail(String senderEmail, String receiverEmail);

	List<ChatMessage> findBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail);

	@Query("SELECT id FROM ChatMessage WHERE receiver_email = :receiverEmail AND is_already_seen = 0 "
			+ "GROUP BY sender_email")
	List<Long> countByReceiverEmailAndIsAlreadySeenGroupBySenderEmail(String receiverEmail);

	@Modifying
	@Transactional
	int deleteBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail);

	@Modifying
	@Transactional
	@Query("UPDATE ChatMessage SET is_already_seen = 1 WHERE (sender_email = :userEmail OR receiver_email = :userEmail)"
			+ " AND (sender_email = :partnerEmail OR receiver_email = :partnerEmail) AND  is_already_seen = 0")
	int updateWithPartnerToAlreadySeenByUserEmail(String userEmail, String partnerEmail);
}
