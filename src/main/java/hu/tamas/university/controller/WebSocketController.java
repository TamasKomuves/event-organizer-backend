package hu.tamas.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.university.dto.ChatMessageDto;
import hu.tamas.university.dto.InvitationDto;
import hu.tamas.university.entity.ChatMessage;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.ChatMessageRepository;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.InvitationRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.Timestamp;

@Controller
public class WebSocketController {

	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final InvitationRepository invitationRepository;
	private final EventRepository eventRepository;

	public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper,
			UserRepository userRepository, ChatMessageRepository chatMessageRepository,
			InvitationRepository invitationRepository,
			EventRepository eventRepository) {
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.objectMapper = objectMapper;
		this.userRepository = userRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.invitationRepository = invitationRepository;
		this.eventRepository = eventRepository;
	}

	@MessageMapping("/send/message")
	public ChatMessageDto sendMessageToUser(final String messageString,
			@Payload Message message) throws IOException {
		final ChatMessageDto chatMessageDto = objectMapper.readValue(messageString, ChatMessageDto.class);
		final String userEmail =
				((UsernamePasswordAuthenticationToken) message.getHeaders().get("simpUser")).getName();
		final User receiver = userRepository.findByEmail(chatMessageDto.getReceiverEmail()).get();
		final User sender = userRepository.findByEmail(userEmail).get();

		final ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSender(sender);
		chatMessage.setReceiver(receiver);
		chatMessage.setText(chatMessageDto.getText());
		chatMessage.setDate(new Timestamp(System.currentTimeMillis()));

		final ChatMessage createdChatMessage = chatMessageRepository.save(chatMessage);
		final ChatMessageDto chatMessageDtoToReturn = ChatMessageDto.fromEntity(createdChatMessage);

		this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + calculateTopicName(chatMessageDto),
				chatMessageDtoToReturn);
		this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + chatMessageDto.getReceiverEmail(),
				chatMessageDtoToReturn);

		return chatMessageDtoToReturn;
	}

	@MessageMapping("/send/invitation")
	public InvitationDto sendInvitation(final String messageString) throws IOException {
		final InvitationDto invitationDto = objectMapper.readValue(messageString, InvitationDto.class);

		final Event event = eventRepository.findEventById(invitationDto.getEventId());
		final User user = userRepository.findByEmail(invitationDto.getUserEmail()).get();

		final Invitation invitation = new Invitation();
		invitation.setEvent(event);
		invitation.setUser(user);
		invitation.setUserRequested(invitationDto.getUserRequested());
		invitation.setAccepted(0);
		invitation.setSentDate(new Timestamp(System.currentTimeMillis()));
		invitation.setIsAlreadySeen(0);

		final InvitationDto savedInvitationDto =
				InvitationDto.fromEntity(invitationRepository.save(invitation));

		this.simpMessagingTemplate
				.convertAndSend("/socket-publisher/invitations" + invitationDto.getUserEmail(),
						savedInvitationDto);
		this.simpMessagingTemplate
				.convertAndSend("/socket-publisher/invitation-counter" + invitationDto.getUserEmail(),
						savedInvitationDto);

		return savedInvitationDto;
	}

	private String calculateTopicName(ChatMessageDto chatMessageDto) {
		final String senderEmail = chatMessageDto.getSenderEmail();
		final String receiverEmail = chatMessageDto.getReceiverEmail();

		return senderEmail.compareTo(receiverEmail) > 0 ? receiverEmail + "-" + senderEmail :
				senderEmail + "-" + receiverEmail;
	}
}
