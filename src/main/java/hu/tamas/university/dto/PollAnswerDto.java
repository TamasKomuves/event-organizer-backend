package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.PollAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PollAnswerDto {

	@JsonProperty("id")
	private int id;

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

	public static PollAnswer fromDto(PollAnswerDto pollAnswerDto) {
		PollAnswer pollAnswer = new PollAnswer();
		pollAnswer.setId(pollAnswerDto.getId());
		pollAnswer.setText(pollAnswerDto.getText());

		return pollAnswer;
	}
}
