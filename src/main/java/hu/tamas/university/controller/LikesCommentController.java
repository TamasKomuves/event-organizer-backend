package hu.tamas.university.controller;

import hu.tamas.university.dto.LikesCommentDto;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.LikesCommentRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public @ResponseBody
	ResponseEntity<LikesCommentDto> getLikesCommentById(@PathVariable int id) {
		LikesComment likesComment = likesCommentRepository.findLikesCommentById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(LikesCommentDto.fromEntity(likesComment), headers, HttpStatus.OK);
	}

	@GetMapping("/create/{userEmail}/{commentId}")
	public @ResponseBody
	ResponseEntity<String> saveLikesPost(@PathVariable String userEmail, @PathVariable int commentId) {

		LikesComment likesComment = new LikesComment();
		likesComment.setUser(userRepository.findByEmail(userEmail).get());
		likesComment.setComment(commentRepository.findCommentById(commentId));

		likesCommentRepository.save(likesComment);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}
}
