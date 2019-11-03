package hu.tamas.university.dto;

import hu.tamas.university.entity.PollAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PollAnswerDto {

	private int id;

	private int pollQuestionId;

	@NotNull
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
