package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

	@JsonProperty
	private boolean isCurrentUserSent;

	@NotNull
	private String partnerEmail;

	public static ChatMessageDto fromEntity(ChatMessage chatMessage, String currentUserEmail) {
		ChatMessageDto chatMessageDto = new ChatMessageDto();

		chatMessageDto.setId(chatMessage.getId());
		chatMessageDto.setText(chatMessage.getText());
		chatMessageDto.setDate(chatMessage.getDate());
		boolean isCurrentUserSent = currentUserEmail.equals(chatMessage.getSender().getEmail());
		chatMessageDto.setCurrentUserSent(isCurrentUserSent);
		chatMessageDto.setPartnerEmail(getPartnerEmail(chatMessage, currentUserEmail));

		return chatMessageDto;
	}

	private static String getPartnerEmail(ChatMessage chatMessage, String currentUserEmail) {
		final String senderEmail = chatMessage.getSender().getEmail();
		final String receiverEmail = chatMessage.getReceiver().getEmail();
		return senderEmail.equals(currentUserEmail) ? receiverEmail : senderEmail;
	}
}
