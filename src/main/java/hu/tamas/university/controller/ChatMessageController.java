package hu.tamas.university.controller;

import com.google.common.collect.Maps;
import hu.tamas.university.dto.ChatMessageDto;
import hu.tamas.university.entity.ChatMessage;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat-messages")
public class ChatMessageController {

	private final ChatMessageRepository chatMessageRepository;

	@Autowired
	public ChatMessageController(ChatMessageRepository chatMessageRepository) {
		this.chatMessageRepository = chatMessageRepository;
	}

	@GetMapping("/all-messages/{userEmail1}/{userEmail2}")
	@ResponseBody
	public List<ChatMessageDto> getAllChatMessages(@AuthenticationPrincipal final User user,
			@PathVariable String userEmail) {
		final String currentUserEmail = user.getEmail();
		final List<ChatMessage> chatMessages = chatMessageRepository
				.findBySenderEmailAndReceiverEmail(currentUserEmail, userEmail);
		final List<ChatMessage> chatMessages2 = chatMessageRepository
				.findBySenderEmailAndReceiverEmail(userEmail, currentUserEmail);
		chatMessages.addAll(chatMessages2);

		return chatMessages.stream().map(chatMessage -> ChatMessageDto.fromEntity(chatMessage, currentUserEmail))
				.collect(Collectors.toList());
	}

	@GetMapping("/last-messages")
	@ResponseBody
	public List<ChatMessageDto> getLastChatMessages(@AuthenticationPrincipal final User user) {
		final String email = user.getEmail();
		final List<ChatMessage> chatMessages = chatMessageRepository.findBySenderEmailOrReceiverEmail(email, email);
		final Map<String, ChatMessage> map = Maps.newHashMap();

		chatMessages.sort(Comparator.comparing(ChatMessage::getDate).reversed());

		for (ChatMessage chatMessage : chatMessages) {
			final String partnerEmail = getPartnerEmail(chatMessage, email);

			if (!map.containsKey(partnerEmail)) {
				map.put(partnerEmail, chatMessage);
			}
		}

		return map.values().stream().map(chatMessage -> ChatMessageDto.fromEntity(chatMessage, email))
				.collect(Collectors.toList());
	}

	private String getPartnerEmail(ChatMessage chatMessage, String currentUserEmail) {
		final String senderEmail = chatMessage.getSender().getEmail();
		final String receiverEmail = chatMessage.getReceiver().getEmail();
		return senderEmail.equals(currentUserEmail) ? receiverEmail : senderEmail;
	}
}
