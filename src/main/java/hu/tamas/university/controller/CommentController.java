package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentController {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Autowired
	public CommentController(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public CommentDto getCommentById(@PathVariable int id) {
		Comment comment = commentRepository.findCommentById(id);

		return CommentDto.fromEntity(comment);
	}

	@GetMapping("/{id}/likers")
	@ResponseBody
	public List<UserDto> getLikers(@PathVariable int id) {
		Comment comment = commentRepository.findCommentById(id);

		return comment.getLikers().stream().map(UserDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("create/{postId}/{commenter_email}/{text}")
	@ResponseBody
	public String saveComment(@PathVariable int postId, @PathVariable String commenter_email, @PathVariable String text) {
		Comment comment = new Comment();
		comment.setPost(postRepository.findPostById(postId));
		comment.setCommenter(userRepository.findByEmail(commenter_email).get());
		comment.setText(text);
		comment.setCommentDate(new Timestamp(System.currentTimeMillis()));

		commentRepository.save(comment);

		return "{\"result\":\"success\"}";
	}

	@GetMapping("{id}/likers/{email}")
	@ResponseBody
	public String isLikedAlready(@PathVariable int id, @PathVariable String email) {
		Comment comment = commentRepository.findCommentById(id);
		int number = comment.getLikers().stream().
				filter(user -> user.getEmail().equals(email)).collect(Collectors.toList()).size();
		String result = number > 0 ? "true" : "false";

		return "{\"result\":\"" + result + "\"}";
	}
}
