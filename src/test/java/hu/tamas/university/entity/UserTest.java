package hu.tamas.university.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

	@Mock
	private Set<Event> organizedEvents;
	@Mock
	private Set<Comment> comments;
	@Mock
	private Set<Post> posts;
	@Mock
	private Event event;
	@Mock
	private Post post;
	@Mock
	private Comment comment;

	private User user;

	@Before
	public void setUp() {
		user = new User();
		user.setOrganizedEvents(organizedEvents);
		user.setPosts(posts);
		user.setComments(comments);
	}

	@Test
	public void testAddOrganizedEvent() {
		user.addOrganizedEvent(event);

		verify(organizedEvents).add(event);
		verify(event).setOrganizer(user);
	}

	@Test
	public void testAddPost() {
		user.addPost(post);

		verify(posts).add(post);
		verify(post).setPoster(user);
	}

	@Test
	public void testAddComment() {
		user.addComment(comment);

		verify(comments).add(comment);
		verify(comment).setCommenter(user);
	}
}
