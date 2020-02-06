package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";

	private final CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public CommentDto getCommentById(@PathVariable int id) {
		return commentService.getCommentById(id);
	}

	@GetMapping("/{id}/likers")
	@ResponseBody
	public List<UserDto> getLikers(@PathVariable int id) {
		return commentService.getLikers(id);
	}

	@PostMapping("/create")
	@ResponseBody
	public String createComment(@RequestBody @Valid CommentDto commentDto,
			@AuthenticationPrincipal User user) {
		commentService.createComment(commentDto, user.getEmail());
		return RESULT_SUCCESS;
	}

	@GetMapping("/{id}/likers/{email}")
	@ResponseBody
	public String isLikedAlready(@PathVariable int id, @PathVariable String email) {
		final boolean isAlreadyLiked = commentService.isLikedAlready(id, email);
		return "{\"result\":\"" + isAlreadyLiked + "\"}";
	}

	@GetMapping("/{id}/add-liker")
	@ResponseBody
	public String addLiker(@PathVariable int id, @AuthenticationPrincipal User user) {
		commentService.addLiker(id, user.getEmail());
		return RESULT_SUCCESS;
	}

	@DeleteMapping("{id}/remove-liker")
	@ResponseBody
	public String removeLiker(@PathVariable int id, @AuthenticationPrincipal User user) {
		commentService.removeLiker(id, user.getEmail());
		return RESULT_SUCCESS;
	}

	@DeleteMapping("{id}/delete")
	@ResponseBody
	public String deleteComment(@PathVariable int id, @AuthenticationPrincipal User user) {
		commentService.deleteComment(id, user.getEmail());
		return RESULT_SUCCESS;
	}
}
