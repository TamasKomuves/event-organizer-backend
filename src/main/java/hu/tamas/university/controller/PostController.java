package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.entity.Post;
import hu.tamas.university.repository.EventRepository;
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
@RequestMapping("/posts")
public class PostController {

	private final PostRepository postRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;

	@Autowired
	public PostController(PostRepository postRepository, EventRepository eventRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<PostDto> getPostById(@PathVariable int id) {
		Post post = postRepository.findPostById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(PostDto.fromEntity(post), headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/comments")
	public @ResponseBody
	ResponseEntity<List<CommentDto>> getAllPosts(@PathVariable int id) {
		Post post = postRepository.findPostById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		List<CommentDto> commentDtos = post.getComments().stream().map(CommentDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(commentDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/likers")
	public @ResponseBody
	ResponseEntity<List<UserDto>> getLikers(@PathVariable int id) {
		Post post = postRepository.findPostById(id);
		List<UserDto> userDtos = post.getLikers().stream().map(UserDto::fromEntity).collect(Collectors.toList());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(userDtos, headers, HttpStatus.OK);
	}

	@GetMapping("create/{eventId}/{poster_email}/{text}")
	public @ResponseBody
	ResponseEntity<String> savePost(@PathVariable int eventId, @PathVariable String poster_email, @PathVariable String text) {
		Post post = new Post();
		post.setEvent(eventRepository.findEventById(eventId));
		post.setPoster(userRepository.findByEmail(poster_email).get());
		post.setText(text);
		post.setPostDate(new Timestamp(System.currentTimeMillis()));

		postRepository.save(post);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("{id}/likers/{email}")
	public @ResponseBody
	ResponseEntity<String> isLikedAlready(@PathVariable int id, @PathVariable String email) {
		Post post = postRepository.findPostById(id);
		int number = post.getLikers().stream().
				filter(user -> user.getEmail().equals(email)).collect(Collectors.toList()).size();
		String result = number > 0 ? "true" : "false";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}
}
