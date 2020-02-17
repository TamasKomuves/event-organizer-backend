package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.AnswersToPollDto;
import hu.tamas.university.dto.PollAnswerDto;
import hu.tamas.university.entity.AnswersToPoll;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AnswersToPollRepository;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.PollQuestionRepository;
import hu.tamas.university.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PollAnswerControllerTest {

	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";
	private static final String USER_EMAIL = "user@gmail.com";
	private static final int POLL_ANSWER_ID = 26;
	private static final int POLL_QUESTION_ID = 142;

	@Mock
	private PollAnswerRepository pollAnswerRepository;
	@Mock
	private AnswersToPollRepository answersToPollRepository;
	@Mock
	private PollQuestionRepository pollQuestionRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private User user;
	@Mock
	private PollAnswer pollAnswer;
	@Mock
	private PollQuestion pollQuestion;
	@Mock
	private AnswersToPoll answersToPoll1;
	@Mock
	private AnswersToPoll answersToPoll2;

	private PollAnswerController controller;

	@Before
	public void setUp() {
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
		when(user.getEmail()).thenReturn(USER_EMAIL);
		when(pollAnswerRepository.findPollAnswerById(POLL_ANSWER_ID)).thenReturn(pollAnswer);
		when(pollAnswer.getId()).thenReturn(POLL_ANSWER_ID);
		when(pollAnswer.getPollQuestion()).thenReturn(pollQuestion);

		controller = new PollAnswerController(pollAnswerRepository, answersToPollRepository,
				pollQuestionRepository, userRepository);
	}

	@Test
	public void testGetPollAnswerById() {
		final PollAnswerDto result = controller.getPollAnswerById(POLL_ANSWER_ID);

		assertEquals(POLL_ANSWER_ID, result.getId());
	}

	@Test
	public void testGetVotesByPollAnswerId() {
		when(answersToPollRepository.findAnswersToPollByPollAnswerId(POLL_ANSWER_ID))
				.thenReturn(Lists.newArrayList(answersToPoll1, answersToPoll2));
		mockAnswersToPolls();

		final List<AnswersToPollDto> result = controller.getVotesByPollAnswerId(POLL_ANSWER_ID);

		assertEquals(2, result.size());
		assertEquals(44, result.get(0).getId());
		assertEquals(75, result.get(1).getId());
	}

	@Test
	public void testIsAlreadySelected_True() {
		when(answersToPollRepository
				.findAnswersToPollByPollAnswerIdAndUserEmail(POLL_ANSWER_ID, USER_EMAIL))
				.thenReturn(Optional.of(answersToPoll1));

		final String result = controller.isAlreadySelected(POLL_ANSWER_ID, user);

		assertEquals("{\"isAlreadySelected\":\"true\"}", result);
	}

	@Test
	public void testIsAlreadySelected_False() {
		when(answersToPollRepository
				.findAnswersToPollByPollAnswerIdAndUserEmail(POLL_ANSWER_ID, USER_EMAIL))
				.thenReturn(Optional.empty());

		final String result = controller.isAlreadySelected(POLL_ANSWER_ID, user);

		assertEquals("{\"isAlreadySelected\":\"false\"}", result);
	}

	@Test
	public void testCreatePollAnswer() {
		when(pollQuestionRepository.findPollQuestionById(POLL_QUESTION_ID)).thenReturn(pollQuestion);
		final PollAnswerDto pollAnswerDto = new PollAnswerDto();
		pollAnswerDto.setPollQuestionId(POLL_QUESTION_ID);

		final String result = controller.createPollAnswer(pollAnswerDto);

		assertEquals(RESULT_SUCCESS, result);
		verify(pollQuestion).addPollAnswer(any(PollAnswer.class));
		verify(pollQuestionRepository).flush();
	}

	@Test
	public void testAddRespondent() {
		when(answersToPollRepository
				.findAnswersToPollByPollAnswerIdAndUserEmail(POLL_ANSWER_ID, USER_EMAIL))
				.thenReturn(Optional.empty());

		final String result = controller.addRespondent(POLL_ANSWER_ID, user);

		assertEquals(RESULT_SUCCESS, result);
		verify(pollAnswer).addRespondent(user);
		verify(pollAnswerRepository).flush();
	}

	@Test
	public void testAddRespondent_AlreadyAnswered() {
		when(answersToPollRepository
				.findAnswersToPollByPollAnswerIdAndUserEmail(POLL_ANSWER_ID, USER_EMAIL))
				.thenReturn(Optional.of(answersToPoll1));

		final String result = controller.addRespondent(POLL_ANSWER_ID, user);

		assertEquals("{\"result\":\"alreadyAnswered\"}", result);
		verify(pollAnswer, never()).addRespondent(user);
		verify(pollAnswerRepository, never()).flush();
	}

	@Test
	public void testRemoveRespondent() {
		final String result = controller.removeRespondent(POLL_ANSWER_ID, user);

		assertEquals(RESULT_SUCCESS, result);
		verify(pollAnswer).removeRespondent(user);
		verify(pollAnswerRepository).flush();
	}

	private void mockAnswersToPolls() {
		when(answersToPoll1.getUser()).thenReturn(user);
		when(answersToPoll1.getId()).thenReturn(44);
		when(answersToPoll1.getPollAnswer()).thenReturn(pollAnswer);
		when(answersToPoll2.getUser()).thenReturn(user);
		when(answersToPoll2.getId()).thenReturn(75);
		when(answersToPoll2.getPollAnswer()).thenReturn(pollAnswer);
	}
}
