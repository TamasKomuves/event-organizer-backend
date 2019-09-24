package hu.tamas.university.controller;

import hu.tamas.university.dto.ChatMessageDto;
import hu.tamas.university.entity.ChatMessage;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.ChatMessageRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat-messages")
public class ChatMessageController {

	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;

	@Autowired
	public ChatMessageController(ChatMessageRepository chatMessageRepository,
			UserRepository userRepository) {
		this.chatMessageRepository = chatMessageRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/all-messages/{userEmail1}/{userEmail2}")
	@ResponseBody
	public List<ChatMessageDto> getAllChatMessages(@AuthenticationPrincipal final User user,
			@PathVariable String userEmail1,
			@PathVariable String userEmail2) {
		List<ChatMessage> chatMessages = chatMessageRepository
				.findBySenderEmailAndReceiverEmail(userEmail1, userEmail2);
		List<ChatMessage> chatMessages2 = chatMessageRepository
				.findBySenderEmailAndReceiverEmail(userEmail2, userEmail1);
		chatMessages.addAll(chatMessages2);

		return chatMessages.stream().map(chatMessage -> ChatMessageDto.fromEntity(chatMessage, user.getEmail()))
				.collect(Collectors.toList());
	}
}
