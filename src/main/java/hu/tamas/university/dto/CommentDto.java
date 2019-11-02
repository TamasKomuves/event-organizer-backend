package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class CommentDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("commenterEmail")
	private String commenterEmail;

	@NotNull
	@JsonProperty("postId")
	private int postId;

	@JsonProperty("commentDate")
	private Timestamp commentDate;

	@NotNull
	@JsonProperty("text")
	private String text;

	public static CommentDto fromEntity(Comment comment) {
		CommentDto commentDto = new CommentDto();
		commentDto.setId(comment.getId());
		if (comment.getCommenter() != null)
			commentDto.setCommenterEmail(comment.getCommenter().getEmail());
		if (comment.getPost() != null)
			commentDto.setPostId(comment.getPost().getId());
		commentDto.setCommentDate(comment.getCommentDate());
		commentDto.setText(comment.getText());
		return commentDto;
	}
}
