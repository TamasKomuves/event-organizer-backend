package hu.tamas.university.dto;

import hu.tamas.university.entity.AnswersToPoll;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AnswersToPollDto {

	private int id;

	private String userEmail;

	@NotNull
	private int pollAnswerId;

	public static AnswersToPollDto fromEntity(final AnswersToPoll answersToPoll) {
		final AnswersToPollDto answersToPollDto = new AnswersToPollDto();
		answersToPollDto.setId(answersToPoll.getId());
		answersToPollDto.setUserEmail(answersToPoll.getUser().getEmail());
		answersToPollDto.setPollAnswerId(answersToPoll.getPollAnswer().getId());

		return answersToPollDto;
	}

	public static List<AnswersToPollDto> fromEntityList(final List<AnswersToPoll> answersToPolls) {
		return answersToPolls.stream().map(AnswersToPollDto::fromEntity).collect(Collectors.toList());
	}
}
