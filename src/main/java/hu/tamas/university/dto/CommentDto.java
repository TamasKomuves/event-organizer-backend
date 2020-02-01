package hu.tamas.university.dto;

import hu.tamas.university.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class CommentDto {

	private int id;

	private String commenterEmail;

	@NotNull
	private int postId;

	private Timestamp commentDate;

	@NotNull
	private String text;

	public static CommentDto fromEntity(final Comment comment) {
		final CommentDto commentDto = new CommentDto();
		commentDto.setId(comment.getId());
		if (comment.getCommenter() != null) {
			commentDto.setCommenterEmail(comment.getCommenter().getEmail());
		}
		if (comment.getPost() != null) {
			commentDto.setPostId(comment.getPost().getId());
		}
		commentDto.setCommentDate(comment.getCommentDate());
		commentDto.setText(comment.getText());
		return commentDto;
	}
}
