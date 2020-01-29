package hu.tamas.university.controller;

import hu.tamas.university.dto.PostDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostControllerTest {

	private static final String USER_EMAIL = "user@gmail.com";
	private static final String POST_TEXT = "text";
	private static final int EVENT_ID = 40;
	private static final String SUCCESS_RESPONSE_TEXT = "{\"result\":\"success\"}";

	@Mock
	private PostRepository postRepository;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private Event event;
	@Mock
	private User user;
	@Mock
	private User poster;

	@Captor
	private ArgumentCaptor<Post> postCaptor;

	private PostDto postDto;

	private PostController postController;

	@Before
	public void setUp() {
		postController = new PostController(postRepository, eventRepository, userRepository);
	}

	@Test
	public void testSavePost() {
		initPostDto();
		when(user.getEmail()).thenReturn(USER_EMAIL);
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(poster));

		final String result = postController.savePost(postDto, user);

		verify(postRepository).saveAndFlush(postCaptor.capture());
		final Post post = postCaptor.getValue();
		verify(event).addPost(post);
		verify(poster).addPost(post);
		assertEquals(SUCCESS_RESPONSE_TEXT, result);
		assertEquals(POST_TEXT, post.getText());
	}

	private void initPostDto() {
		postDto = new PostDto();
		postDto.setEventId(EVENT_ID);
		postDto.setText(POST_TEXT);
	}
}
