package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Comment;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommenterEmail() {
		return commenterEmail;
	}

	public void setCommenterEmail(String commenterEmail) {
		this.commenterEmail = commenterEmail;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public Timestamp getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Timestamp commentDate) {
		this.commentDate = commentDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
