package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.PollQuestion;

import java.sql.Timestamp;

public class PollQuestionDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("eventId")
	private int eventId;

	@JsonProperty("text")
	private String text;

	@JsonProperty("date")
	private Timestamp date;

	public static PollQuestionDto fromEntity(PollQuestion pollQuestion) {
		PollQuestionDto pollQuestionDto = new PollQuestionDto();
		pollQuestionDto.setId(pollQuestion.getId());
		pollQuestionDto.setEventId(pollQuestion.getEvent().getId());
		pollQuestionDto.setText(pollQuestion.getText());
		pollQuestionDto.setDate(pollQuestion.getDate());
		return pollQuestionDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
}
