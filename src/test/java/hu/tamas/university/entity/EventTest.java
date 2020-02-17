package hu.tamas.university.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventTest {

	@Mock
	private Set<ParticipateInEvent> participateInEvents;
	@Mock
	private Set<ParticipateInEvent> userParticipateInEvents;
	@Mock
	private Set<Post> posts;
	@Mock
	private Set<PollQuestion> pollQuestions;
	@Mock
	private User user;
	@Mock
	private Post post;
	@Mock
	private PollQuestion pollQuestion;

	@Captor
	private ArgumentCaptor<ParticipateInEvent> participateInEventCaptor;

	private Event event;

	@Before
	public void setUp() {
		when(user.getParticipateInEvents()).thenReturn(userParticipateInEvents);

		event = new Event();
		event.setParticipateInEvents(participateInEvents);
		event.setPosts(posts);
		event.setPollQuestions(pollQuestions);
	}

	@Test
	public void testAddParticipant() {
		event.addParticipant(user);

		verify(participateInEvents).add(participateInEventCaptor.capture());
		verify(participateInEvents).add(participateInEventCaptor.getValue());
	}

	@Test
	public void testAddPost() {
		event.addPost(post);

		verify(posts).add(post);
		verify(post).setEvent(event);
	}

	@Test
	public void testAddPollQuestion() {
		event.addPollQuestion(pollQuestion);

		verify(pollQuestions).add(pollQuestion);
		verify(pollQuestion).setEvent(event);
	}
}
