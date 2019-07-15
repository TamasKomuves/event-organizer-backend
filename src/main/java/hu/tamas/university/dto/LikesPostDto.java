package hu.tamas.university.dto;

import hu.tamas.university.entity.LikesPost;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;

import javax.validation.constraints.NotNull;

public class LikesPostDto {

	private int id;

	@NotNull
	private User user;

	@NotNull
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
