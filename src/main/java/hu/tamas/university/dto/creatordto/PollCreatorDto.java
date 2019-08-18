package hu.tamas.university.dto.creatordto;

import hu.tamas.university.dto.PollAnswerDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PollCreatorDto {

	@NotNull
	private int eventId;

	@NotNull
	private String questionText;

	@NotNull
	private List<PollAnswerDto> pollAnswers;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<PollAnswerDto> getPollAnswers() {
		return pollAnswers;
	}

	public void setPollAnswers(List<PollAnswerDto> pollAnswers) {
		this.pollAnswers = pollAnswers;
	}
}
