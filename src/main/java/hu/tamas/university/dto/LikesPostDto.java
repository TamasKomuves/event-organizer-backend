package hu.tamas.university.dto;

import hu.tamas.university.entity.LikesPost;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
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
}
