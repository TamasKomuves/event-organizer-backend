package hu.tamas.university.controller;

import hu.tamas.university.dto.LikesCommentDto;
import hu.tamas.university.dto.creatordto.LikesCommentCreatorDto;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.LikesCommentRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

	@PostMapping("/create")
	@ResponseBody
	public String saveLikesPost(@RequestBody @Valid LikesCommentCreatorDto likesCommentCreatorDto) {
		User user = userRepository.findByEmail(likesCommentCreatorDto.getUserEmail()).orElse(null);
		Comment comment = commentRepository.findCommentById(likesCommentCreatorDto.getCommentId());

		LikesComment likesComment = new LikesComment();
		likesComment.setUser(user);
		likesComment.setComment(comment);

		likesCommentRepository.save(likesComment);

		return "{\"result\":\"success\"}";
	}
}
