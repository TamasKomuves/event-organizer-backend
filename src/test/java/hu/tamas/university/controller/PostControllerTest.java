package hu.tamas.university.controller;

import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.service.PostService;
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
public class PostControllerTest {

	private static final int POST_ID = 43;
	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";
	private static final String CURRENT_USER_EMAIL = "current@gmail.com";

	@Mock
	private PostService postService;
	@Mock
	private PostDto postDto;
	@Mock
	private User currentUser;
	@Mock
	private List<CommentDto> commentDtoList;
	@Mock
	private List<UserDto> likers;

	private PostController postController;

	@Before
	public void setUp() {
		when(currentUser.getEmail()).thenReturn(CURRENT_USER_EMAIL);
		postController = new PostController(postService);
	}

	@Test
	public void testGetPostById() {
		when(postService.getPostById(POST_ID)).thenReturn(postDto);

		final PostDto result = postController.getPostById(POST_ID);

		assertEquals(postDto, result);
	}

	@Test
	public void testGetAllComments() {
		when(postService.getAllComments(POST_ID)).thenReturn(commentDtoList);

		final List<CommentDto> result = postController.getAllComments(POST_ID);

		assertEquals(commentDtoList, result);
	}

	@Test
	public void testGetLikers() {
		when(postService.getLikers(POST_ID)).thenReturn(likers);

		final List<UserDto> result = postController.getLikers(POST_ID);

		assertEquals(likers, result);
	}

	@Test
	public void testSavePost() {
		final String result = postController.savePost(postDto, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(postService).savePost(postDto, CURRENT_USER_EMAIL);
	}

	@Test
	public void testIsLikedAlready_True() {
		when(postService.isLikedAlready(POST_ID, CURRENT_USER_EMAIL)).thenReturn(true);

		final String result = postController.isLikedAlready(POST_ID, CURRENT_USER_EMAIL);

		assertEquals("{\"result\":\"true\"}", result);
	}

	@Test
	public void testIsLikedAlready_False() {
		when(postService.isLikedAlready(POST_ID, CURRENT_USER_EMAIL)).thenReturn(false);

		final String result = postController.isLikedAlready(POST_ID, CURRENT_USER_EMAIL);

		assertEquals("{\"result\":\"false\"}", result);
	}

	@Test
	public void testAddLiker() {
		final String result = postController.addLiker(POST_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(postService).addLiker(POST_ID, CURRENT_USER_EMAIL);
	}

	@Test
	public void testRemoveLiker() {
		final String result = postController.removeLiker(POST_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(postService).removeLiker(POST_ID, CURRENT_USER_EMAIL);
	}

	@Test
	public void testDeletePost() {
		final String result = postController.deletePost(POST_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(postService).deletePost(POST_ID, CURRENT_USER_EMAIL);
	}
}
