package hu.tamas.university.dto;

import hu.tamas.university.entity.Comment;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesCommentDto {

	private int id;

	private User user;

	private Comment comment;

	public static LikesCommentDto fromEntity(LikesComment likesComment) {
		LikesCommentDto likesCommentDto = new LikesCommentDto();
		likesCommentDto.setId(likesComment.getId());
		likesCommentDto.setUser(likesComment.getUser());
		likesCommentDto.setComment(likesComment.getComment());
		return likesCommentDto;
	}
}
