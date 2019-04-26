package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.PostRepository;
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
	public @ResponseBody
	ResponseEntity<CommentDto> getCommentById(@PathVariable int id) {
		Comment comment = commentRepository.findCommentById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(CommentDto.fromEntity(comment), headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/likers")
	public @ResponseBody
	ResponseEntity<List<UserDto>> getLikers(@PathVariable int id) {
		Comment comment = commentRepository.findCommentById(id);
		List<UserDto> userDtos = comment.getLikers().stream().map(UserDto::fromEntity).collect(Collectors.toList());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(userDtos, headers, HttpStatus.OK);
	}

	@GetMapping("create/{postId}/{commenter_email}/{text}")
	public @ResponseBody
	ResponseEntity<String> saveComment(@PathVariable int postId, @PathVariable String commenter_email, @PathVariable String text) {
		Comment comment = new Comment();
		comment.setPost(postRepository.findPostById(postId));
		comment.setCommenter(userRepository.findByEmail(commenter_email).get());
		comment.setText(text);
		comment.setCommentDate(new Timestamp(System.currentTimeMillis()));

		commentRepository.save(comment);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("{id}/likers/{email}")
	public @ResponseBody
	ResponseEntity<String> isLikedAlready(@PathVariable int id, @PathVariable String email) {
		Comment comment = commentRepository.findCommentById(id);
		int number = comment.getLikers().stream().
				filter(user -> user.getEmail().equals(email)).collect(Collectors.toList()).size();
		String result = number > 0 ? "true" : "false";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}
}
