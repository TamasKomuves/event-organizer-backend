package hu.tamas.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.university.dto.ChatMessageDto;
import hu.tamas.university.entity.ChatMessage;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.ChatMessageRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.sql.Timestamp;

@Controller
public class ChatMessageSocketController {

	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;
	private final ChatMessageRepository chatMessageRepository;

	public ChatMessageSocketController(SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper,
			UserRepository userRepository, ChatMessageRepository chatMessageRepository) {
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.objectMapper = objectMapper;
		this.userRepository = userRepository;
		this.chatMessageRepository = chatMessageRepository;
	}

	@MessageMapping("/send/message")
	public ChatMessageDto sendMessageToUser(final String messageString) throws IOException {
		final ChatMessageDto chatMessageDto = objectMapper.readValue(messageString, ChatMessageDto.class);

		final ChatMessage chatMessage = new ChatMessage();
		final User receiver = userRepository.findByEmail(chatMessageDto.getReceiverEmail()).get();
		final User sender = userRepository.findByEmail(chatMessageDto.getSenderEmail()).get();

		chatMessage.setSender(sender);
		chatMessage.setReceiver(receiver);
		chatMessage.setText(chatMessageDto.getText());
		chatMessage.setDate(new Timestamp(System.currentTimeMillis()));

		final ChatMessage createdChatMessage = chatMessageRepository.save(chatMessage);
		final ChatMessageDto chatMessageDtoToReturn = ChatMessageDto.fromEntity(createdChatMessage);

		this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + calculateTopicName(chatMessageDto),
				chatMessageDtoToReturn);

		return chatMessageDtoToReturn;
	}

	private String calculateTopicName(ChatMessageDto chatMessageDto) {
		final String senderEmail = chatMessageDto.getSenderEmail();
		final String receiverEmail = chatMessageDto.getReceiverEmail();

		return senderEmail.compareTo(receiverEmail) > 0 ? receiverEmail + "-" + senderEmail :
				senderEmail + "-" + receiverEmail;
	}
}
