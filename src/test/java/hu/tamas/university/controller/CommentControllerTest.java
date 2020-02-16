package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {

	private static final int COMMENT_ID = 16;
	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";
	private static final String CURRENT_USER_EMAIL = "current@gmail.com";

	@Mock
	private CommentService commentService;
	@Mock
	private User currentUser;
	@Mock
	private CommentDto commentDto;
	@Mock
	private List<UserDto> likers;

	private CommentController commentController;

	@Before
	public void setUp() {
		when(currentUser.getEmail()).thenReturn(CURRENT_USER_EMAIL);

		commentController = new CommentController(commentService);
	}

	@Test
	public void testGetCommentById() {
		when(commentService.getCommentById(COMMENT_ID)).thenReturn(commentDto);

		final CommentDto result = commentController.getCommentById(COMMENT_ID);

		assertEquals(commentDto, result);
	}

	@Test
	public void testGetLikers() {
		when(commentService.getLikers(COMMENT_ID)).thenReturn(likers);

		final List<UserDto> result = commentController.getLikers(COMMENT_ID);

		assertEquals(likers, result);
	}

	@Test
	public void testCreateComment() {
		final String result = commentController.createComment(commentDto, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(commentService).createComment(commentDto, CURRENT_USER_EMAIL);
	}

	@Test
	public void testIsLikedAlready_True() {
		when(commentService.isLikedAlready(COMMENT_ID, CURRENT_USER_EMAIL)).thenReturn(true);

		final String result = commentController.isLikedAlready(COMMENT_ID, CURRENT_USER_EMAIL);

		assertEquals("{\"result\":\"true\"}", result);
	}

	@Test
	public void testIsLikedAlready_False() {
		when(commentService.isLikedAlready(COMMENT_ID, CURRENT_USER_EMAIL)).thenReturn(false);

		final String result = commentController.isLikedAlready(COMMENT_ID, CURRENT_USER_EMAIL);

		assertEquals("{\"result\":\"false\"}", result);
	}

	@Test
	public void testAddLiker() {
		final String result = commentController.addLiker(COMMENT_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(commentService).addLiker(COMMENT_ID, CURRENT_USER_EMAIL);
	}

	@Test
	public void testRemoveLiker() {
		final String result = commentController.removeLiker(COMMENT_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(commentService).removeLiker(COMMENT_ID, CURRENT_USER_EMAIL);
	}

	@Test
	public void testDeleteComment() {
		final String result = commentController.deleteComment(COMMENT_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(commentService).deleteComment(COMMENT_ID, CURRENT_USER_EMAIL);
	}
}
