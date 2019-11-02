package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.AnswersToPoll;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
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

	public static List<AnswersToPollDto> fromEntityList(List<AnswersToPoll> answersToPolls) {
		return answersToPolls.stream().map(AnswersToPollDto::fromEntity).collect(Collectors.toList());
	}
}
