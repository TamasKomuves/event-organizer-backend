package hu.tamas.university.service;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.*;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.LikesCommentRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

	private static final int COMMENT_ID = 3;
	private static final int POST_ID = 5;
	private static final String COMMENTER_EMAIL = "email@gmail.com";
	private static final String COMMENT_TEXT = "testText";
	private static final String LIKER_EMAIL = "liker@gmail.com";

	@Mock
	private CommentRepository commentRepository;
	@Mock
	private LikesCommentRepository likesCommentRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	@Mock
	private EntityManager entityManager;
	@Mock
	private LikesComment likesComment;
	@Mock
	private Post post;
	@Mock
	private User user;
	@Mock
	private Event event;
	@Mock
	private Transaction transaction;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Comment comment;

	private CommentService commentService;

	@Before
	public void setUp() {
		mockComment();
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

		commentService = new CommentService(commentRepository, likesCommentRepository, postRepository,
				userRepository, entityManagerFactory);
	}

	private void mockComment() {
		when(comment.getId()).thenReturn(COMMENT_ID);
		when(comment.getPost()).thenReturn(mock(Post.class));
		when(comment.getPost().getId()).thenReturn(POST_ID);
		when(comment.getCommenter()).thenReturn(mock(User.class));
		when(comment.getCommenter().getEmail()).thenReturn(COMMENTER_EMAIL);
		when(comment.getText()).thenReturn(COMMENT_TEXT);
	}

	@Test
	public void testGetCommentById() {
		when(commentRepository.findCommentById(COMMENT_ID)).thenReturn(comment);

		final CommentDto result = commentService.getCommentById(COMMENT_ID);

		assertEquals(COMMENT_ID, result.getId());
		assertEquals(COMMENT_TEXT, result.getText());
		assertEquals(COMMENTER_EMAIL, result.getCommenterEmail());
		assertEquals(POST_ID, result.getPostId());
	}

	@Test
	public void testGetLikers() {
		final User user = new User();
		user.setEmail(LIKER_EMAIL);
		when(likesCommentRepository.findByCommentId(COMMENT_ID))
				.thenReturn(Optional.of(Lists.newArrayList(likesComment)));
		when(likesComment.getUser()).thenReturn(user);

		final List<UserDto> likers = commentService.getLikers(COMMENT_ID);

		assertEquals(1, likers.size());
		assertEquals(LIKER_EMAIL, likers.get(0).getEmail());
	}

	@Test
	public void testCreateComment() {
		final CommentDto commentDto = new CommentDto();
		commentDto.setPostId(POST_ID);
		commentDto.setCommenterEmail(COMMENTER_EMAIL);
		when(postRepository.findPostById(POST_ID)).thenReturn(post);
		when(userRepository.findByEmail(COMMENTER_EMAIL)).thenReturn(Optional.of(user));

		commentService.createComment(commentDto, COMMENTER_EMAIL);

		verify(post).addComment(any(Comment.class));
		verify(user).addComment(any(Comment.class));
		verify(commentRepository).saveAndFlush(any(Comment.class));
	}

	@Test
	public void testIsLikedAlready_True() {
		when(likesCommentRepository
				.findByCommentIdAndUserEmail(COMMENT_ID, COMMENTER_EMAIL))
				.thenReturn(Optional.of(likesComment));

		final boolean result = commentService.isLikedAlready(COMMENT_ID, COMMENTER_EMAIL);

		assertTrue(result);
	}

	@Test
	public void testIsLikedAlready_False() {
		when(likesCommentRepository
				.findByCommentIdAndUserEmail(COMMENT_ID, COMMENTER_EMAIL))
				.thenReturn(Optional.empty());

		final boolean result = commentService.isLikedAlready(COMMENT_ID, COMMENTER_EMAIL);

		assertFalse(result);
	}

	@Test
	public void testAddLiker() {
		when(commentRepository.findCommentById(COMMENT_ID)).thenReturn(comment);
		when(userRepository.findByEmail(LIKER_EMAIL)).thenReturn(Optional.of(user));

		commentService.addLiker(COMMENT_ID, LIKER_EMAIL);

		verify(comment).addLiker(user);
		verify(commentRepository).flush();
	}

	@Test
	public void testRemoveLiker() {
		when(commentRepository.findCommentById(COMMENT_ID)).thenReturn(comment);
		when(userRepository.findByEmail(LIKER_EMAIL)).thenReturn(Optional.of(user));

		commentService.removeLiker(COMMENT_ID, LIKER_EMAIL);

		verify(comment).removeLiker(user);
		verify(commentRepository).saveAndFlush(comment);
	}

	@Test
	public void testDeleteComment_HasPermission() {
		when(commentRepository.findCommentById(COMMENT_ID)).thenReturn(comment);
		when(comment.getCommenter()).thenReturn(user);
		when(comment.getPost()).thenReturn(post);
		when(post.getEvent()).thenReturn(event);
		when(event.getOrganizer()).thenReturn(user);
		when(user.getEmail()).thenReturn(COMMENTER_EMAIL);
		when(entityManager.getTransaction()).thenReturn(transaction);

		commentService.deleteComment(COMMENT_ID, COMMENTER_EMAIL);

		verify(transaction).begin();
		verify(likesCommentRepository).deleteByCommentIdIn(Collections.singletonList(COMMENT_ID));
		verify(transaction).commit();
	}

	@Test
	public void testDeleteComment_HasNoPermission() {
		when(commentRepository.findCommentById(COMMENT_ID)).thenReturn(comment);
		when(comment.getCommenter()).thenReturn(null);
		when(comment.getPost()).thenReturn(post);
		when(post.getEvent()).thenReturn(event);
		when(event.getOrganizer()).thenReturn(mock(User.class));
		when(user.getEmail()).thenReturn(COMMENTER_EMAIL);

		try {
			commentService.deleteComment(COMMENT_ID, COMMENTER_EMAIL);
		} catch (RuntimeException e) {
			assertEquals("no permission", e.getMessage());
		}
	}

	@Test
	public void testDeleteCommentsOfPosts() {
		when(commentRepository.findIdsByPostIds(Lists.newArrayList(POST_ID)))
				.thenReturn(Optional.of(Lists.newArrayList(COMMENT_ID)));

		commentService.deleteCommentsOfPosts(Lists.newArrayList(POST_ID));

		verify(likesCommentRepository).deleteByCommentIdIn(Lists.newArrayList(COMMENT_ID));
		verify(commentRepository).deleteByPostIdIn(Lists.newArrayList(POST_ID));
	}
}
