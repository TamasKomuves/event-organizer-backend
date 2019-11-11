package hu.tamas.university.service;

import hu.tamas.university.entity.Event;
import hu.tamas.university.repository.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private final EntityManager entityManager;
	private final ParticipateInEventRepository participateInEventRepository;
	private final InvitationRepository invitationRepository;
	private final LikesCommentRepository likesCommentRepository;
	private final LikesPostRepository likesPostRepository;
	private final AnswersToPollRepository answersToPollRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final EventService eventService;

	public UserService(EntityManagerFactory entityManagerFactory,
			ParticipateInEventRepository participateInEventRepository,
			InvitationRepository invitationRepository,
			LikesCommentRepository likesCommentRepository,
			LikesPostRepository likesPostRepository,
			AnswersToPollRepository answersToPollRepository,
			ChatMessageRepository chatMessageRepository,
			CommentRepository commentRepository, PostRepository postRepository,
			EventRepository eventRepository, UserRepository userRepository,
			EventService eventService) {
		entityManager = entityManagerFactory.createEntityManager();
		this.participateInEventRepository = participateInEventRepository;
		this.invitationRepository = invitationRepository;
		this.likesCommentRepository = likesCommentRepository;
		this.likesPostRepository = likesPostRepository;
		this.answersToPollRepository = answersToPollRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.eventService = eventService;
	}

	@Transactional
	public String deleteUser(String userEmail) {
		final EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();
			executeQueriesToDeleteUser(userEmail);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}

		return "success";
	}

	private void executeQueriesToDeleteUser(String userEmail) {
		participateInEventRepository.deleteByUserEmail(userEmail);
		invitationRepository.deleteByUserEmail(userEmail);
		likesCommentRepository.deleteByUserEmail(userEmail);
		likesPostRepository.deleteByUserEmail(userEmail);
		answersToPollRepository.deleteByUserEmail(userEmail);
		chatMessageRepository.deleteBySenderEmailOrReceiverEmail(userEmail, userEmail);
		commentRepository.updateByUserEmail(userEmail);
		postRepository.updateByUserEmail(userEmail);

		deleteEventsOrganizedByTheUser(userEmail);

		userRepository.deleteByEmail(userEmail);
	}

	private void deleteEventsOrganizedByTheUser(String userEmail) {
		final Optional<List<Event>> organizedEvents = eventRepository.findByOrganizerEmail(userEmail);
		organizedEvents.ifPresent(
				events -> events.forEach(event -> eventService.deleteEvent(event.getId(), userEmail)));
	}
}
