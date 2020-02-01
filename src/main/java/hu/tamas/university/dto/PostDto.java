package hu.tamas.university.dto;

import hu.tamas.university.entity.Post;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PostDto extends NewsDto {

	private static final String TYPE = "POST";

	@NotNull
	private int eventId;

	private String posterEmail;

	@NotNull
	private String text;

	public static PostDto fromEntity(final Post post) {
		final PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setEventId(post.getEvent().getId());
		if (post.getPoster() != null) {
			postDto.setPosterEmail(post.getPoster().getEmail());
		}
		postDto.setDate(post.getPostDate());
		postDto.setText(post.getText());
		return postDto;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
