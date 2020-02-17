package hu.tamas.university.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentTest {

	@Mock
	private Set<LikesComment> likesComments;
	@Mock
	private Set<LikesComment> userLikesComments;
	@Mock
	private User user;

	@Captor
	private ArgumentCaptor<LikesComment> likesCommentCaptor;

	private Comment comment;

	@Before
	public void setUp() {
		when(user.getLikesComments()).thenReturn(userLikesComments);

		comment = new Comment();
		comment.setLikesComments(likesComments);
		comment.setCommenter(user);
	}

	@Test
	public void testAddLiker() {
		comment.addLiker(user);

		verify(likesComments).add(likesCommentCaptor.capture());
		verify(userLikesComments).add(likesCommentCaptor.getValue());
	}

	@Test
	public void testRemoveLiker() {
		comment.removeLiker(user);

		verify(likesComments).remove(likesCommentCaptor.capture());
		final LikesComment capturedLikesComment = likesCommentCaptor.getValue();
		verify(userLikesComments).remove(capturedLikesComment);
		assertNull(capturedLikesComment.getUser());
		assertNull(capturedLikesComment.getComment());
	}
}