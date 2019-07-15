package hu.tamas.university.dto.creatordto;

import javax.validation.constraints.NotNull;

public class LikesCommentCreatorDto {

	@NotNull
	private String userEmail;

	@NotNull
	private int commentId;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
}
