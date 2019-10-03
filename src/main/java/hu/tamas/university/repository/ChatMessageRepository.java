package hu.tamas.university.repository;

import hu.tamas.university.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

	ChatMessage findById(int id);

	List<ChatMessage> findBySenderEmailAndReceiverEmail(String senderEmail, String receiverEmail);

	List<ChatMessage> findBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail);
}
