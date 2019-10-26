package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.LikesCommentRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentController {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikesCommentRepository likesCommentRepository;

	@Autowired
	public CommentController(CommentRepository commentRepository, PostRepository postRepository,
			UserRepository userRepository, LikesCommentRepository likesCommentRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.likesCommentRepository = likesCommentRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public CommentDto getCommentById(@PathVariable int id) {
		final Comment comment = commentRepository.findCommentById(id);

		return CommentDto.fromEntity(comment);
	}

	@GetMapping("/{id}/likers")
	@ResponseBody
	public List<UserDto> getLikers(@PathVariable int id) {
		final List<LikesComment> likesComments = likesCommentRepository.findByCommentId(id)
				.orElse(Lists.newArrayList());

		return likesComments.stream().map(likesComment -> UserDto.fromEntity(likesComment.getUser()))
				.collect(Collectors.toList());
	}

	@GetMapping("create/{postId}/{commenter_email}/{text}")
	@ResponseBody
	public String saveComment(@PathVariable int postId, @PathVariable String commenter_email,
			@PathVariable String text) {
		final Post post = postRepository.findPostById(postId);
		final User user = userRepository.findByEmail(commenter_email).orElse(null);

		final Comment comment = new Comment();
		comment.setText(text);
		comment.setCommentDate(new Timestamp(System.currentTimeMillis()));
		post.addComment(comment);
		user.addComment(comment);

		commentRepository.saveAndFlush(comment);

		return "{\"result\":\"success\"}";
	}

	@GetMapping("{id}/likers/{email}")
	@ResponseBody
	public String isLikedAlready(@PathVariable int id, @PathVariable String email) {
		final Optional<LikesComment> likesComment = likesCommentRepository.findByCommentIdAndUserEmail(id, email);

		return "{\"result\":\"" + likesComment.isPresent() + "\"}";
	}

	@GetMapping("{id}/add-liker")
	@ResponseBody
	public String addLiker(@PathVariable int id, @AuthenticationPrincipal User user) {
		final Comment comment = commentRepository.findCommentById(id);
		final User liker = userRepository.findByEmail(user.getEmail()).get();

		comment.addLiker(liker);
		commentRepository.flush();

		return "{\"result\":\"success\"}";
	}
}
