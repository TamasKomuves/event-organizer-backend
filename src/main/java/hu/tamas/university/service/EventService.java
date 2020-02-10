package hu.tamas.university.service;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.OrganizerRatingDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.EventRating;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class EventService {

	private final EventRepository eventRepository;
	private final LikesCommentRepository likesCommentRepository;
	private final CommentRepository commentRepository;
	private final LikesPostRepository likesPostRepository;
	private final PollAnswerRepository pollAnswerRepository;
	private final AnswersToPollRepository answersToPollRepository;
	private final PollQuestionRepository pollQuestionRepository;
	private final PostRepository postRepository;
	private final ParticipateInEventRepository participateInEventRepository;
	private final InvitationRepository invitationRepository;
	private final EventRatingRepository eventRatingRepository;
	private final UserRepository userRepository;
	private final EntityManager entityManager;

	@Autowired
	public EventService(EventRepository eventRepository,
			LikesCommentRepository likesCommentRepository,
			CommentRepository commentRepository, LikesPostRepository likesPostRepository,
			PollAnswerRepository pollAnswerRepository,
			AnswersToPollRepository answersToPollRepository,
			PollQuestionRepository pollQuestionRepository, PostRepository postRepository,
			ParticipateInEventRepository participateInEventRepository,
			InvitationRepository invitationRepository,
			EventRatingRepository eventRatingRepository,
			UserRepository userRepository,
			EntityManagerFactory entityManagerFactory) {
		this.eventRepository = eventRepository;
		this.likesCommentRepository = likesCommentRepository;
		this.commentRepository = commentRepository;
		this.likesPostRepository = likesPostRepository;
		this.pollAnswerRepository = pollAnswerRepository;
		this.answersToPollRepository = answersToPollRepository;
		this.pollQuestionRepository = pollQuestionRepository;
		this.postRepository = postRepository;
		this.participateInEventRepository = participateInEventRepository;
		this.invitationRepository = invitationRepository;
		this.eventRatingRepository = eventRatingRepository;
		this.userRepository = userRepository;
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	@Transactional
	public String deleteEvent(int eventId, String loggedInUserEmail) {
		final Event event = eventRepository.findEventById(eventId);

		if (event == null) {
			return "no such event";
		}

		final User organizer = event.getOrganizer();
		if (organizer == null || !organizer.getEmail().equals(loggedInUserEmail)) {
			return "no permission";
		}

		final List<Integer> postIds = postRepository.findIdByEventId(eventId).orElse(Lists.newArrayList(-1));
		final List<Integer> commentIds = commentRepository.findIdsByPostIds(postIds)
				.orElse(Lists.newArrayList(-1));
		final List<Integer> pollQuestionIds = pollQuestionRepository.findIdsByEventId(eventId)
				.orElse(Lists.newArrayList(-1));
		final List<Integer> pollAnswerIds = pollAnswerRepository.findIdByPollQuestionIds(pollQuestionIds)
				.orElse(Lists.newArrayList(-1));

		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			executeDeleteEventQueries(commentIds, postIds, pollAnswerIds, pollQuestionIds, eventId);
			transaction.commit();
			return "success";
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	@Transactional
	private void executeDeleteEventQueries(List<Integer> commentIds, List<Integer> postIds,
			List<Integer> pollAnswerIds,
			List<Integer> pollQuestionIds, int eventId) {
		likesCommentRepository.deleteByCommentIdIn(commentIds);
		commentRepository.deleteByPostIdIn(postIds);
		likesPostRepository.deleteByPostIdIn(postIds);
		postRepository.deleteByEventId(eventId);
		answersToPollRepository.deleteByPollAnswerIdIn(pollAnswerIds);
		pollAnswerRepository.deleteByPollQuestionIdIn(pollQuestionIds);
		pollQuestionRepository.deleteByEventId(eventId);
		participateInEventRepository.deleteByEventId(eventId);
		invitationRepository.deleteByEventId(eventId);
		eventRatingRepository.deleteByEventId(eventId);
		eventRepository.deleteById(eventId);
	}

	public double getRatingOfUser(final int eventId, final String userEmail) {
		return eventRatingRepository //
				.findByEventIdAndRaterEmail(eventId, userEmail) //
				.orElseThrow(() -> new RuntimeException("no rating")) //
				.getRating();
	}

	public void saveEventRating(final int eventId, final double rating,
			String userEmail) {
		final User user = userRepository.findByEmail(userEmail).get();
		final Event event = eventRepository.findEventById(eventId);
		final EventRating eventRating = eventRatingRepository //
				.findByEventIdAndRaterEmail(eventId, userEmail) //
				.orElse(new EventRating(user, event));
		eventRating.setRating(rating);
		eventRatingRepository.save(eventRating);
	}

	public void removeParticipant(final int id, final String email, final String currentUserEmail) {
		final Event event = eventRepository.findEventById(id);
		final User organizer = event.getOrganizer();
		if ((organizer == null || !currentUserEmail.equals(organizer.getEmail())) && !email
				.equals(currentUserEmail)) {
			throw new RuntimeException("no permission");
		}
		participateInEventRepository.deleteByEventIdAndUserEmail(id, email);
	}

	public OrganizerRatingDto getOrganizerReputation(final int eventId) {
		final Event event = eventRepository.findById(eventId).get();
		final User organizer = event.getOrganizer();
		if (organizer == null) {
			return null;
		}

		final String organizerEmail = organizer.getEmail();
		final List<Integer> eventIds = eventRepository.findIdByOrganizerEmail(organizerEmail);
		if (eventIds.isEmpty()) {
			return null;
		}

		final List<EventRating> eventRatings = eventRatingRepository.findByEventIdIn(eventIds);
		final int numberOfRatings = eventRatings.size();
		if (numberOfRatings == 0) {
			return null;
		}
		double ratingSum = 0;

		for (EventRating eventRating : eventRatings) {
			ratingSum += eventRating.getRating();
		}

		return new OrganizerRatingDto(numberOfRatings, ratingSum / numberOfRatings);
	}
}
