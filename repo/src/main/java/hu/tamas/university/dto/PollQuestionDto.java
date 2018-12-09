package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.PollQuestion;

import java.sql.Timestamp;

public class PollQuestionDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("event")
	private Event event;

	@JsonProperty("text")
	private String text;

	@JsonProperty("pollQuestionDate")
	private Timestamp pollQuestionDate;

	public static PollQuestionDto fromEntity(PollQuestion pollQuestion) {
		PollQuestionDto pollQuestionDto = new PollQuestionDto();
		pollQuestionDto.setId(pollQuestion.getId());
		pollQuestionDto.setEvent(pollQuestion.getEvent());
		pollQuestionDto.setText(pollQuestion.getText());
		pollQuestionDto.setPollQuestionDate(pollQuestion.getPollQuestionDate());
		return pollQuestionDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getPollQuestionDate() {
		return pollQuestionDate;
	}

	public void setPollQuestionDate(Timestamp pollQuestionDate) {
		this.pollQuestionDate = pollQuestionDate;
	}
}
