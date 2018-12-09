package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.entity.User;

public class LikesCommentDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("user")
	private User user;

	@JsonProperty("comment")
	private Comment comment;

	public static LikesCommentDto fromEntity(LikesComment likesComment) {
		LikesCommentDto likesCommentDto = new LikesCommentDto();
		likesCommentDto.setId(likesComment.getId());
		likesCommentDto.setUser(likesComment.getUser());
		likesCommentDto.setComment(likesComment.getComment());
		return likesCommentDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
