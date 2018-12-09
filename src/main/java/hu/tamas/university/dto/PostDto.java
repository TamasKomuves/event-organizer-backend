package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Post;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

public class PostDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("eventId")
	private int eventId;

	@JsonProperty("posterEmail")
	private String posterEmail;

	@JsonProperty("postDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp postDate;

	@JsonProperty("text")
	private String text;

	public static PostDto fromEntity(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		if (post.getEvent() != null)
			postDto.setEventId(post.getEvent().getId());
		postDto.setPosterEmail(post.getPoster().getEmail());
		postDto.setPosttDate(post.getPostDate());
		postDto.setText(post.getText());
		return postDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getPosterEmail() {
		return posterEmail;
	}

	public void setPosterEmail(String posterEmail) {
		this.posterEmail = posterEmail;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setPosttDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
