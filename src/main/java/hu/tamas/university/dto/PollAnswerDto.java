package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;

import javax.validation.constraints.NotNull;

public class PollAnswerDto {

	@JsonProperty("id")
	private int id;

	@NotNull
	@JsonProperty("pollQuestionId")
	private int pollQuestionId;

	@NotNull
	@JsonProperty("text")
	private String text;

	public static PollAnswerDto fromEntity(PollAnswer pollAnswer) {
		PollAnswerDto pollAnswerDto = new PollAnswerDto();
		pollAnswerDto.setId(pollAnswer.getId());
		pollAnswerDto.setPollQuestionId(pollAnswer.getPollQuestion().getId());
		pollAnswerDto.setText(pollAnswer.getText());
		return pollAnswerDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPollQuestionId() {
		return pollQuestionId;
	}

	public void setPollQuestionId(int pollQuestion) {
		this.pollQuestionId = pollQuestion;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
