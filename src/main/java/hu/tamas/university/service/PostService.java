package hu.tamas.university.service;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.LikesPostRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final LikesPostRepository likesPostRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final CommentService commentService;
	private final EntityManager entityManager;

	@Autowired
	public PostService(PostRepository postRepository, LikesPostRepository likesPostRepository,
			EventRepository eventRepository, UserRepository userRepository, CommentService commentService,
			EntityManagerFactory entityManagerFactory) {
		this.postRepository = postRepository;
		this.likesPostRepository = likesPostRepository;
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.commentService = commentService;
		this.entityManager = entityManagerFactory.createEntityManager();
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

	public void deletePost(final int id, final String userEmail) {
		if (!hasPermissionToDeletePost(id, userEmail)) {
			throw new RuntimeException("no permission");
		}

		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			commentService.deleteCommentsOfPosts(Collections.singletonList(id));
			likesPostRepository.deleteByPostIdIn(Collections.singletonList(id));
			postRepository.deleteById(id);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deletePostsOfEvent(final int eventId) {
		final List<Integer> postIds = postRepository.findIdByEventId(eventId).orElse(null);
		if (postIds == null || postIds.isEmpty()) {
			return;
		}

		commentService.deleteCommentsOfPosts(postIds);
		likesPostRepository.deleteByPostIdIn(postIds);
		postRepository.deleteByEventId(eventId);
	}

	private boolean hasPermissionToDeletePost(final int id, final String userEmail) {
		final Post post = postRepository.findPostById(id);
		final User poster = post.getPoster();
		final User organizer = post.getEvent().getOrganizer();
		final String posterEmail = poster == null ? null : poster.getEmail();
		final String organizerEmail = organizer == null ? null : organizer.getEmail();

		return userEmail.equals(posterEmail) || userEmail.equals(organizerEmail);
	}
}
