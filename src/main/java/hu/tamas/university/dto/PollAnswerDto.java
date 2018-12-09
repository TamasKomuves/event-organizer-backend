package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;

public class PollAnswerDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("pollQuestion")
	private PollQuestion pollQuestion;

	@JsonProperty("text")
	private String text;

	public static PollAnswerDto fromEntity(PollAnswer pollAnswer) {
		PollAnswerDto pollAnswerDto = new PollAnswerDto();
		pollAnswerDto.setId(pollAnswer.getId());
		pollAnswerDto.setPollQuestion(pollAnswer.getPollQuestion());
		pollAnswerDto.setText(pollAnswer.getText());
		return pollAnswerDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PollQuestion getPollQuestion() {
		return pollQuestion;
	}

	public void setPollQuestion(PollQuestion pollQuestion) {
		this.pollQuestion = pollQuestion;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
