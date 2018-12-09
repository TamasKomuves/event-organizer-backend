package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.AnswersToPoll;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.User;

public class AnswersToPollDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("user")
	private User user;

	@JsonProperty("pollAnswer")
	private PollAnswer pollAnswer;

	public static AnswersToPollDto fromEntity(AnswersToPoll answersToPoll) {
		AnswersToPollDto answersToPollDto = new AnswersToPollDto();
		answersToPollDto.setId(answersToPoll.getId());
		answersToPollDto.setUser(answersToPoll.getUser());
		answersToPollDto.setPollAnswer(answersToPoll.getPollAnswer());
		return answersToPollDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PollAnswer getPollAnswer() {
		return pollAnswer;
	}

	public void setPollAnswer(PollAnswer pollAnswer) {
		this.pollAnswer = pollAnswer;
	}
}
