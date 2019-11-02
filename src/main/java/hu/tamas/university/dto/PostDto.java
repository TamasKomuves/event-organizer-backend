package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Post;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PostDto extends NewsDto {

	private static final String TYPE = "POST";

	@NotNull
	@JsonProperty("eventId")
	private int eventId;

	@JsonProperty("posterEmail")
	private String posterEmail;

	@NotNull
	@JsonProperty("text")
	private String text;

	public static PostDto fromEntity(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		if (post.getEvent() != null)
			postDto.setEventId(post.getEvent().getId());
		postDto.setPosterEmail(post.getPoster().getEmail());
		postDto.setDate(post.getPostDate());
		postDto.setText(post.getText());
		return postDto;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
