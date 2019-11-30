package hu.tamas.university.dto;

import hu.tamas.university.entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class ChatMessageDto {

	private int id;

	private Timestamp date;

	@NotNull
	private String text;

	@NotNull
	private String receiverEmail;

	private String senderEmail;

	public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
		final ChatMessageDto chatMessageDto = new ChatMessageDto();

		chatMessageDto.setId(chatMessage.getId());
		chatMessageDto.setText(chatMessage.getText());
		chatMessageDto.setDate(chatMessage.getDate());
		chatMessageDto.setSenderEmail(chatMessage.getSender().getEmail());
		chatMessageDto.setReceiverEmail(chatMessage.getReceiver().getEmail());

		return chatMessageDto;
	}

	private static String getPartnerEmail(ChatMessage chatMessage, String currentUserEmail) {
		final String senderEmail = chatMessage.getSender().getEmail();
		final String receiverEmail = chatMessage.getReceiver().getEmail();
		return senderEmail.equals(currentUserEmail) ? receiverEmail : senderEmail;
	}
}
