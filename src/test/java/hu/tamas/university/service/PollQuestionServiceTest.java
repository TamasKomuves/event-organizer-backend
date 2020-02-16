package hu.tamas.university.service;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.PollAnswerDto;
import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.dto.creatordto.PollCreatorDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AnswersToPollRepository;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.PollQuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PollQuestionServiceTest {

	private static final int POLL_QUESTION_ID = 4;
	private static final int EVENT_ID = 2;
	private static final String ORGANIZER_EMAIL = "organizer@gmail.com";

	@Mock
	private PollQuestionRepository pollQuestionRepository;
	@Mock
	private PollAnswerRepository pollAnswerRepository;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private AnswersToPollRepository answersToPollRepository;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	@Mock
	private EntityManager entityManager;
	@Mock
	private PollQuestion pollQuestion;
	@Mock
	private Event event;
	@Mock
	private PollAnswer pollAnswer1;
	@Mock
	private PollAnswer pollAnswer2;
	@Mock
	private User organizer;
	@Mock
	private EntityTransaction transaction;

	private PollQuestionService pollQuestionService;

	@Before
	public void setUp() {
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

		pollQuestionService = new PollQuestionService(pollQuestionRepository, pollAnswerRepository,
				eventRepository, answersToPollRepository, entityManagerFactory);
	}

	@Test
	public void testGetPollQuestionById() {
		when(pollQuestionRepository.findPollQuestionById(POLL_QUESTION_ID)).thenReturn(pollQuestion);
		when(pollQuestion.getEvent()).thenReturn(event);
		when(pollQuestion.getId()).thenReturn(POLL_QUESTION_ID);

		final PollQuestionDto result = pollQuestionService.getPollQuetionById(POLL_QUESTION_ID);

		assertEquals(POLL_QUESTION_ID, result.getId());
	}

	@Test
	public void testGetPollAnswerIdsForQuestion() {
		final ArrayList<PollAnswer> pollAnswers = Lists.newArrayList(pollAnswer1, pollAnswer2);
		when(pollAnswerRepository.findPollAnswersByPollQuestionId(POLL_QUESTION_ID)).thenReturn(
				pollAnswers);
		when(pollAnswer1.getId()).thenReturn(12);
		when(pollAnswer2.getId()).thenReturn(13);

		final List<Integer> result = pollQuestionService
				.getPollAnswerIdsForQuestion(POLL_QUESTION_ID);

		assertEquals(2, result.size());
		assertEquals(12, (int) result.get(0));
		assertEquals(13, (int) result.get(1));
	}

	@Test
	public void testCreatePoll() {
		final PollCreatorDto pollCreatorDto = new PollCreatorDto();
		pollCreatorDto.setEventId(EVENT_ID);
		pollCreatorDto.setQuestionText("QUESTION_TEXT");
		final PollAnswerDto pollAnswerDto = new PollAnswerDto();
		pollAnswerDto.setPollQuestionId(POLL_QUESTION_ID);
		pollCreatorDto.setPollAnswers(Lists.newArrayList(pollAnswerDto));
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);

		pollQuestionService.createPoll(pollCreatorDto);

		verify(event).addPollQuestion(any(PollQuestion.class));
		verify(eventRepository).flush();
	}

	@Test
	public void testDeletePollQuestion() {
		when(pollQuestionRepository.findPollQuestionById(POLL_QUESTION_ID)).thenReturn(pollQuestion);
		when(pollQuestion.getEvent()).thenReturn(event);
		when(event.getOrganizer()).thenReturn(organizer);
		when(organizer.getEmail()).thenReturn(ORGANIZER_EMAIL);
		when(entityManager.getTransaction()).thenReturn(transaction);
		when(pollAnswerRepository
				.findIdByPollQuestionIds(Collections.singletonList(POLL_QUESTION_ID))).thenReturn(
				Optional.of(Lists.newArrayList(5)));

		pollQuestionService.deletePollQuestion(POLL_QUESTION_ID, ORGANIZER_EMAIL);

		verify(transaction).begin();
		verify(answersToPollRepository).deleteByPollAnswerIdIn(Lists.newArrayList(5));
		verify(pollAnswerRepository).deleteByPollQuestionIdIn(Lists.newArrayList(POLL_QUESTION_ID));
		verify(pollQuestionRepository).deleteById(POLL_QUESTION_ID);
		verify(transaction).commit();
	}
}