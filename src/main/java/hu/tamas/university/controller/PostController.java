package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {

	private final PostRepository postRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;

	@Autowired
	public PostController(PostRepository postRepository, EventRepository eventRepository,
			UserRepository userRepository) {
		this.postRepository = postRepository;
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PostDto getPostById(@PathVariable int id) {
		Post post = postRepository.findPostById(id);
		return PostDto.fromEntity(post);
	}

	@GetMapping("/{id}/comments")
	@ResponseBody
	public List<CommentDto> getAllPosts(@PathVariable int id) {
		Post post = postRepository.findPostById(id);
		return post.getComments().stream().map(CommentDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{id}/likers")
	@ResponseBody
	public List<UserDto> getLikers(@PathVariable int id) {
		Post post = postRepository.findPostById(id);
		return post.getLikesPosts().stream().map(likesPost -> UserDto.fromEntity(likesPost.getUser()))
				.collect(Collectors.toList());
	}

	@GetMapping("create/{eventId}/{poster_email}/{text}")
	@ResponseBody
	public String savePost(@PathVariable int eventId, @PathVariable String poster_email, @PathVariable String text) {
		Post post = new Post();
		post.setEvent(eventRepository.findEventById(eventId));
		post.setPoster(userRepository.findByEmail(poster_email).get());
		post.setText(text);
		post.setPostDate(new Timestamp(System.currentTimeMillis()));

		postRepository.save(post);

		return "{\"result\":\"success\"}";
	}

	@GetMapping("{id}/likers/{email}")
	@ResponseBody
	public String isLikedAlready(@PathVariable int id, @PathVariable String email) {
		Post post = postRepository.findPostById(id);
		boolean isLikedAlready = post.getLikesPosts().stream()
				.anyMatch(likesPost -> Objects.equals(likesPost.getUser().getEmail(), email));
		return "{\"result\":\"" + isLikedAlready + "\"}";
	}

	@GetMapping("{id}/add-liker")
	@ResponseBody
	public String addLiker(@PathVariable int id, @AuthenticationPrincipal User user) {
		final Post post = postRepository.findById(id).get();
		final User liker = userRepository.findByEmail(user.getEmail()).get();

		post.addLiker(liker);
		postRepository.flush();

		return "{\"result\":\"success\"}";
	}
}
