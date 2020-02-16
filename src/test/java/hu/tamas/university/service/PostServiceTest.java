package hu.tamas.university.service;

import com.google.common.collect.Sets;
import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.entity.*;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.LikesPostRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

	private static final int POST_ID = 4;
	private static final int EVENT_ID = 13;
	private static final int COMMENT_ID = 20;
	private static final String POSTER_EMAIL = "poster@gmail.com";
	private static final String LIKER_EMAIL = "liker@gmail.com";

	@Mock
	private PostRepository postRepository;
	@Mock
	private LikesPostRepository likesPostRepository;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CommentService commentService;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	@Mock
	private EntityManager entityManager;
	@Mock
	private Post post;
	@Mock
	private Event event;
	@Mock
	private Comment comment1;
	@Mock
	private User poster;
	@Mock
	private User liker;
	@Mock
	private LikesPost likesPost;

	@Captor
	private ArgumentCaptor<Post> postCaptor;

	private PostService postService;

	@Before
	public void setUp() {
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
		when(postRepository.findPostById(POST_ID)).thenReturn(post);

		postService = new PostService(postRepository, likesPostRepository, eventRepository, userRepository,
				commentService, entityManagerFactory);
	}

	@Test
	public void testGetPostById() {
		when(post.getEvent()).thenReturn(event);
		when(post.getId()).thenReturn(POST_ID);

		final PostDto result = postService.getPostById(POST_ID);

		assertEquals(POST_ID, result.getId());
	}

	@Test
	public void testGetAllComments() {
		when(post.getComments()).thenReturn(Sets.newHashSet(comment1));
		when(comment1.getId()).thenReturn(COMMENT_ID);

		final List<CommentDto> result = postService.getAllComments(POST_ID);

		assertEquals(1, result.size());
		assertEquals(COMMENT_ID, result.get(0).getId());
	}

	@Test
	public void testSavePost() {
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);
		when(userRepository.findByEmail(POSTER_EMAIL)).thenReturn(Optional.of(poster));
		final PostDto postDto = new PostDto();
		postDto.setEventId(EVENT_ID);
		postDto.setPosterEmail(POSTER_EMAIL);
		postDto.setText("POST_TEXT");

		postService.savePost(postDto, POSTER_EMAIL);

		verify(postRepository).saveAndFlush(postCaptor.capture());
		final Post resultPost = postCaptor.getValue();
		assertEquals(resultPost.getText(), "POST_TEXT");
		verify(event).addPost(resultPost);
		verify(poster).addPost(resultPost);
	}

	@Test
	public void testIsLikedAlready_True() {
		when(post.getLikesPosts()).thenReturn(Sets.newHashSet(likesPost));
		when(likesPost.getUser()).thenReturn(poster);
		when(poster.getEmail()).thenReturn(POSTER_EMAIL);

		final boolean result = postService.isLikedAlready(POST_ID, POSTER_EMAIL);

		assertTrue(result);
	}

	@Test
	public void testIsLikedAlready_False() {
		when(post.getLikesPosts()).thenReturn(Sets.newHashSet(likesPost));
		when(likesPost.getUser()).thenReturn(poster);
		when(poster.getEmail()).thenReturn("random@gmail.com");

		final boolean result = postService.isLikedAlready(POST_ID, POSTER_EMAIL);

		assertFalse(result);
	}

	@Test
	public void testAddLiker() {
		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(userRepository.findByEmail(LIKER_EMAIL)).thenReturn(Optional.of(liker));

		postService.addLiker(POST_ID, LIKER_EMAIL);

		verify(post).addLiker(liker);
		verify(postRepository).flush();
	}

	@Test
	public void testRemoveLiker() {
		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(userRepository.findByEmail(LIKER_EMAIL)).thenReturn(Optional.of(liker));

		postService.removeLiker(POST_ID, LIKER_EMAIL);

		verify(post).removeLiker(liker);
		verify(postRepository).saveAndFlush(post);
	}
}
