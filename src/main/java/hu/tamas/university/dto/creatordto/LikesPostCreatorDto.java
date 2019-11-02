package hu.tamas.university.dto.creatordto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LikesPostCreatorDto {

	@NotNull
	private String userEmail;

	@NotNull
	private int postId;
}
