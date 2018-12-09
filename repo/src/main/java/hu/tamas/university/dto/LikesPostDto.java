package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.LikesPost;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;

public class LikesPostDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("user")
	private User user;

	@JsonProperty("post")
	private Post post;

	public static LikesPostDto fromEntity(LikesPost likesPost) {
		LikesPostDto likesPostDto = new LikesPostDto();
		likesPostDto.setId(likesPost.getId());
		likesPostDto.setUser(likesPost.getUser());
		likesPostDto.setPost(likesPost.getPost());
		return likesPostDto;
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

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
