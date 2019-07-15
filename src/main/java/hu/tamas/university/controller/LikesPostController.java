package hu.tamas.university.controller;

import hu.tamas.university.dto.LikesPostDto;
import hu.tamas.university.dto.creatordto.LikesPostCreatorDto;
import hu.tamas.university.entity.LikesPost;
import hu.tamas.university.repository.LikesPostRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
	@ResponseBody
	public LikesPostDto getLikesPostById(@PathVariable int id) {
		LikesPost likesPost = likesPostRepository.findLikesPostById(id);

		return LikesPostDto.fromEntity(likesPost);
	}

	@PostMapping("/create")
	@ResponseBody
	public String createLikesPost(@RequestBody @Valid LikesPostCreatorDto likesPostCreatorDto) {

		LikesPost likesPost = new LikesPost();
		likesPost.setUser(userRepository.findByEmail(likesPostCreatorDto.getUserEmail()).orElse(null));
		likesPost.setPost(postRepository.findPostById(likesPostCreatorDto.getPostId()));

		likesPostRepository.save(likesPost);

		return "{\"result\":\"success\"}";
	}
}
