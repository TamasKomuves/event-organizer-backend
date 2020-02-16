package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.dto.creatordto.PollCreatorDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.service.PollQuestionService;
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
public class PollQuestionControllerTest {

	private static final int POLL_QUESTION_ID = 14;
	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";
	private static final String CURRENT_USER_EMAIL = "current@gmail.com";

	@Mock
	private PollQuestionService pollQuestionService;
	@Mock
	private User currentUser;
	@Mock
	private PollQuestionDto pollQuestionDto;
	@Mock
	private PollCreatorDto pollCreatorDto;

	private PollQuestionController pollQuestionController;

	@Before
	public void setUp() {
		when(currentUser.getEmail()).thenReturn(CURRENT_USER_EMAIL);

		pollQuestionController = new PollQuestionController(pollQuestionService);
	}

	@Test
	public void testGetPollQuestionById() {
		when(pollQuestionService.getPollQuetionById(POLL_QUESTION_ID)).thenReturn(pollQuestionDto);

		final PollQuestionDto result = pollQuestionController.getPollQuetionById(POLL_QUESTION_ID);

		assertEquals(pollQuestionDto, result);
	}

	@Test
	public void testGetPollAnswerIdsForQuestion() {
		when(pollQuestionService.getPollAnswerIdsForQuestion(POLL_QUESTION_ID)).thenReturn(
				Lists.newArrayList(3, 4, 2, 1));

		final List<Integer> result = pollQuestionController.getPollAnswerIdsForQuestion(POLL_QUESTION_ID);

		assertEquals(4, result.size());
		assertEquals(3, (int) result.get(0));
		assertEquals(1, (int) result.get(3));
	}

	@Test
	public void testCreatePoll() {
		final String result = pollQuestionController.createPoll(pollCreatorDto);

		assertEquals(RESULT_SUCCESS, result);
		verify(pollQuestionService).createPoll(pollCreatorDto);
	}

	@Test
	public void testDeletePollQuestion() {
		final String result = pollQuestionController.deletePollQuestion(POLL_QUESTION_ID, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(pollQuestionService).deletePollQuestion(POLL_QUESTION_ID, CURRENT_USER_EMAIL);
	}
}
