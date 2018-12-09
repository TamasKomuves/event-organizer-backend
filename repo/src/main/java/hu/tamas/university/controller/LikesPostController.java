package hu.tamas.university.controller;

import hu.tamas.university.dto.LikesPostDto;
import hu.tamas.university.entity.LikesPost;
import hu.tamas.university.repository.LikesPostRepository;
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

@Controller
@RequestMapping("/likes-posts")
public class LikesPostController {

	private final LikesPostRepository likesPostRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Autowired
	public LikesPostController(LikesPostRepository likesPostRepository, UserRepository userRepository, PostRepository postRepository) {
		this.likesPostRepository = likesPostRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<LikesPostDto> getLikesPostById(@PathVariable int id) {
		LikesPost likesPost = likesPostRepository.findLikesPostById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(LikesPostDto.fromEntity(likesPost), headers, HttpStatus.OK);
	}

	@GetMapping("/create/{userEmail}/{postId}")
	public @ResponseBody
	ResponseEntity<String> saveLikesPost(@PathVariable String userEmail, @PathVariable int postId) {

		LikesPost likesPost = new LikesPost();
		likesPost.setUser(userRepository.findByEmail(userEmail));
		likesPost.setPost(postRepository.findPostById(postId));

		likesPostRepository.save(likesPost);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}
}
