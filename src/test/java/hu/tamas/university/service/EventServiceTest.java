package hu.tamas.university.service;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.OrganizerRatingDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.EventRating;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

	private static final int EVENT_ID = 4;
	private static final String ORGANIZER_EMAIL = "organizer@gmail.com";
	private static final String USER_EMAIL = "user@gmail.com";

	@Mock
	private EventRepository eventRepository;
	@Mock
	private ParticipateInEventRepository participateInEventRepository;
	@Mock
	private InvitationRepository invitationRepository;
	@Mock
	private EventRatingRepository eventRatingRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PostService postService;
	@Mock
	private PollQuestionService pollQuestionService;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	@Mock
	private EntityManager entityManager;
	@Mock
	private Event event;
	@Mock
	private User organizer;
	@Mock
	private User user;
	@Mock
	private EntityTransaction transaction;
	@Mock
	private EventRating eventRating;

	private EventService eventService;

	@Before
	public void setUp() {
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

		eventService = new EventService(eventRepository, postService, participateInEventRepository,
				userRepository, invitationRepository, eventRatingRepository, pollQuestionService,
				entityManagerFactory);
	}

	@Test
	public void testDeleteEvent() {
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);
		when(event.getOrganizer()).thenReturn(organizer);
		when(organizer.getEmail()).thenReturn(ORGANIZER_EMAIL);
		when(entityManager.getTransaction()).thenReturn(transaction);

		eventService.deleteEvent(EVENT_ID, ORGANIZER_EMAIL);

		verify(transaction).begin();
		verify(postService).deletePostsOfEvent(EVENT_ID);
		verify(pollQuestionService).deletePollQuestionsOfEvent(EVENT_ID);
		verify(participateInEventRepository).deleteByEventId(EVENT_ID);
		verify(invitationRepository).deleteByEventId(EVENT_ID);
		verify(eventRatingRepository).deleteByEventId(EVENT_ID);
		verify(eventRepository).deleteById(EVENT_ID);
		verify(transaction).commit();
	}

	@Test
	public void testDeleteEvent_NoPermission() {
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);
		when(event.getOrganizer()).thenReturn(organizer);
		when(organizer.getEmail()).thenReturn(ORGANIZER_EMAIL);
		when(entityManager.getTransaction()).thenReturn(transaction);

		try {
			eventService.deleteEvent(EVENT_ID, USER_EMAIL);
		} catch (RuntimeException e) {
			assertEquals("no permission", e.getMessage());
		}
	}

	@Test
	public void testSaveEventRating() {
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);
		when(eventRatingRepository.findByEventIdAndRaterEmail(EVENT_ID, USER_EMAIL))
				.thenReturn(Optional.of(eventRating));

		final double eventRatingNumber = 7.5;
		eventService.saveEventRating(EVENT_ID, eventRatingNumber, USER_EMAIL);

		verify(eventRating).setRating(eventRatingNumber);
		verify(eventRatingRepository).save(eventRating);
	}

	@Test
	public void testRemoveParticipant() {
		when(eventRepository.findEventById(EVENT_ID)).thenReturn(event);
		when(event.getOrganizer()).thenReturn(organizer);
		when(organizer.getEmail()).thenReturn(ORGANIZER_EMAIL);

		eventService.removeParticipant(EVENT_ID, USER_EMAIL, ORGANIZER_EMAIL);

		verify(participateInEventRepository).deleteByEventIdAndUserEmail(EVENT_ID, USER_EMAIL);
	}

	@Test
	public void testGetOrganizerReputation() {

		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(event.getOrganizer()).thenReturn(organizer);
		when(organizer.getEmail()).thenReturn(ORGANIZER_EMAIL);
		when(eventRepository.findIdByOrganizerEmail(ORGANIZER_EMAIL))
				.thenReturn(Lists.newArrayList(EVENT_ID));
		final EventRating rating = new EventRating(user, event);
		rating.setRating(7.5);
		when(eventRatingRepository.findByEventIdIn(Lists.newArrayList(EVENT_ID)))
				.thenReturn(Lists.newArrayList(rating));

		final OrganizerRatingDto result = eventService.getOrganizerReputation(EVENT_ID);

		assertEquals(1, result.getNumberOfRatings());
		assertTrue(7.5 == result.getAverageRating());
	}
}
