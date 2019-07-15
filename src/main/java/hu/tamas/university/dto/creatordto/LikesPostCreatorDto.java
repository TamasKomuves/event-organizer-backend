package hu.tamas.university.dto.creatordto;

import javax.validation.constraints.NotNull;

public class LikesPostCreatorDto {

	@NotNull
	private String userEmail;

	@NotNull
	private int postId;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}
}
