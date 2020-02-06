package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";

	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PostDto getPostById(@PathVariable int id) {
		return postService.getPostById(id);
	}

	@GetMapping("/{id}/comments")
	@ResponseBody
	public List<CommentDto> getAllComments(@PathVariable int id) {
		return postService.getAllComments(id);
	}

	@GetMapping("/{id}/likers")
	@ResponseBody
	public List<UserDto> getLikers(@PathVariable int id) {
		return postService.getLikers(id);
	}

	@PostMapping("/create")
	@ResponseBody
	public String savePost(@RequestBody PostDto postDto, @AuthenticationPrincipal User user) {
		postService.savePost(postDto, user.getEmail());
		return RESULT_SUCCESS;
	}

	@GetMapping("{id}/likers/{email}")
	@ResponseBody
	public String isLikedAlready(@PathVariable int id, @PathVariable String email) {
		boolean isLikedAlready = postService.isLikedAlready(id, email);
		return "{\"result\":\"" + isLikedAlready + "\"}";
	}

	@GetMapping("{id}/add-liker")
	@ResponseBody
	public String addLiker(@PathVariable int id, @AuthenticationPrincipal User user) {
		postService.addLiker(id, user.getEmail());

		return RESULT_SUCCESS;
	}

	@DeleteMapping("{id}/remove-liker")
	@ResponseBody
	public String removeLiker(@PathVariable int id, @AuthenticationPrincipal User user) {
		postService.removeLiker(id, user.getEmail());
		return RESULT_SUCCESS;
	}

	@DeleteMapping("{id}/delete")
	@ResponseBody
	public String deletePost(@PathVariable int id, @AuthenticationPrincipal User user) {
		postService.deletePost(id, user.getEmail());
		return RESULT_SUCCESS;
	}
}
