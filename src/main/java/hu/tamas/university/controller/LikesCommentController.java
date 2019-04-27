package hu.tamas.university.controller;

import hu.tamas.university.dto.LikesCommentDto;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.LikesCommentRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/likes-comments")
public class LikesCommentController {

	private final LikesCommentRepository likesCommentRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	@Autowired
	public LikesCommentController(LikesCommentRepository likesCommentRepository, UserRepository userRepository, CommentRepository commentRepository) {
		this.likesCommentRepository = likesCommentRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public LikesCommentDto getLikesCommentById(@PathVariable int id) {
		LikesComment likesComment = likesCommentRepository.findLikesCommentById(id);

		return LikesCommentDto.fromEntity(likesComment);
	}

	@GetMapping("/create/{userEmail}/{commentId}")
	@ResponseBody
	public String saveLikesPost(@PathVariable String userEmail, @PathVariable int commentId) {

		LikesComment likesComment = new LikesComment();
		likesComment.setUser(userRepository.findByEmail(userEmail).get());
		likesComment.setComment(commentRepository.findCommentById(commentId));

		likesCommentRepository.save(likesComment);

		return "{\"result\":\"success\"}";
	}
}
