package hu.tamas.university.service;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;

	@Autowired
	public PostService(PostRepository postRepository,
			EventRepository eventRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
	}

	public PostDto getPostById(final int id) {
		final Post post = postRepository.findPostById(id);
		return PostDto.fromEntity(post);
	}

	public List<CommentDto> getAllComments(final int id) {
		final Post post = postRepository.findPostById(id);
		return post.getComments().stream() //
				.map(CommentDto::fromEntity) //
				.collect(Collectors.toList());
	}

	public List<UserDto> getLikers(final int id) {
		final Post post = postRepository.findPostById(id);
		return post.getLikesPosts().stream() //
				.map(likesPost -> UserDto.fromEntity(likesPost.getUser())) //
				.collect(Collectors.toList());
	}

	public void savePost(final PostDto postDto, final String userEmail) {
		final Event event = eventRepository.findEventById(postDto.getEventId());
		final User poster = userRepository.findByEmail(userEmail).get();
		final Post post = new Post();

		post.setText(postDto.getText());
		post.setPostDate(new Timestamp(System.currentTimeMillis()));
		event.addPost(post);
		poster.addPost(post);

		postRepository.saveAndFlush(post);
	}

	public boolean isLikedAlready(final int id, final String userEmail) {
		final Post post = postRepository.findPostById(id);
		return post.getLikesPosts().stream() //
				.map(likesPost -> likesPost.getUser().getEmail()) //
				.anyMatch(likerEmail -> Objects.equals(likerEmail, userEmail));
	}

	public void addLiker(final int id, final String userEmail) {
		final Post post = postRepository.findById(id).get();
		final User liker = userRepository.findByEmail(userEmail).get();

		post.addLiker(liker);
		postRepository.flush();
	}

	public void removeLiker(final int id, final String userEmail) {
		final Post post = postRepository.findById(id).get();
		final User liker = userRepository.findByEmail(userEmail).get();

		post.removeLiker(liker);
		postRepository.saveAndFlush(post);
	}
}
