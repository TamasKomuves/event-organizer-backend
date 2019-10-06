package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.ChatMessage;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class ChatMessageDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("date")
	private Timestamp date;

	@NotNull
	@JsonProperty("text")
	private String text;

	@JsonProperty
	private boolean isCurrentUserSent;

	@NotNull
	@JsonProperty("partnerEmail")
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCurrentUserSent() {
		return isCurrentUserSent;
	}

	public void setCurrentUserSent(boolean currentUserSent) {
		isCurrentUserSent = currentUserSent;
	}

	public String getPartnerEmail() {
		return partnerEmail;
	}

	public void setPartnerEmail(String partnerEmail) {
		this.partnerEmail = partnerEmail;
	}
}
