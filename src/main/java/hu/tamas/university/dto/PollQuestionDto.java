package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.PollQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollQuestionDto extends NewsDto {

	private static final String TYPE = "POLL";

	@JsonProperty("eventId")
	private int eventId;

	@JsonProperty("text")
	private String text;

	public static PollQuestionDto fromEntity(PollQuestion pollQuestion) {
		PollQuestionDto pollQuestionDto = new PollQuestionDto();
		pollQuestionDto.setId(pollQuestion.getId());
		pollQuestionDto.setEventId(pollQuestion.getEvent().getId());
		pollQuestionDto.setText(pollQuestion.getText());
		pollQuestionDto.setDate(pollQuestion.getDate());
		return pollQuestionDto;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
