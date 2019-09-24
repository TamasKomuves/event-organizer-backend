package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.ChatMessage;

import java.sql.Timestamp;

public class ChatMessageDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("date")
	private Timestamp date;

	@JsonProperty("text")
	private String text;

	@JsonProperty
	private boolean isCurrentUserSent;

	public static ChatMessageDto fromEntity(ChatMessage chatMessage, String currentUserEmail) {
		ChatMessageDto chatMessageDto = new ChatMessageDto();

		chatMessageDto.setId(chatMessage.getId());
		chatMessageDto.setText(chatMessage.getText());
		chatMessageDto.setDate(chatMessage.getDate());
		boolean isCurrentUserSent = currentUserEmail.equals(chatMessage.getSender().getEmail());
		chatMessageDto.setCurrentUserSent(isCurrentUserSent);

		return chatMessageDto;
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
}
