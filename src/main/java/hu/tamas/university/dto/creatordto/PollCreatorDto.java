package hu.tamas.university.dto.creatordto;

import hu.tamas.university.dto.PollAnswerDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PollCreatorDto {

	@NotNull
	private int eventId;

	@NotNull
	private String questionText;

	@NotNull
	private List<PollAnswerDto> pollAnswers;
}
