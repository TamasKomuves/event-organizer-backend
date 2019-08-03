package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.AnswersToPoll;

import javax.validation.constraints.NotNull;

public class AnswersToPollDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("userEmail")
	private String userEmail;

	@NotNull
	@JsonProperty("pollAnswerId")
	private int pollAnswerId;

	public static AnswersToPollDto fromEntity(AnswersToPoll answersToPoll) {
		AnswersToPollDto answersToPollDto = new AnswersToPollDto();
		answersToPollDto.setId(answersToPoll.getId());
		answersToPollDto.setUserEmail(answersToPoll.getUser().getEmail());
		answersToPollDto.setPollAnswerId(answersToPoll.getPollAnswer().getId());

		return answersToPollDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getPollAnswerId() {
		return pollAnswerId;
	}

	public void setPollAnswerId(int pollAnswerId) {
		this.pollAnswerId = pollAnswerId;
	}
}
