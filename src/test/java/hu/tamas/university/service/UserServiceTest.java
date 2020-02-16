package hu.tamas.university.service;

import hu.tamas.university.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	private static final String USER_EMAIL = "user@gmail.com";

	@Mock
	private EntityManager entityManager;
	@Mock
	private ParticipateInEventRepository participateInEventRepository;
	@Mock
	private InvitationRepository invitationRepository;
	@Mock
	private LikesCommentRepository likesCommentRepository;
	@Mock
	private LikesPostRepository likesPostRepository;
	@Mock
	private AnswersToPollRepository answersToPollRepository;
	@Mock
	private ChatMessageRepository chatMessageRepository;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	@Mock
	private EntityTransaction transaction;

	private UserService userService;

	@Before
	public void setUp() {
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

		userService = new UserService(entityManagerFactory, participateInEventRepository,
				invitationRepository, likesCommentRepository, likesPostRepository, answersToPollRepository,
				chatMessageRepository, commentRepository, postRepository, eventRepository, userRepository);
	}

	@Test
	public void testDeleteUser() {
		when(entityManager.getTransaction()).thenReturn(transaction);

		userService.deleteUser(USER_EMAIL);

		verify(transaction).begin();
		verify(participateInEventRepository).deleteByUserEmail(USER_EMAIL);
		verify(invitationRepository).deleteByUserEmail(USER_EMAIL);
		verify(likesCommentRepository).deleteByUserEmail(USER_EMAIL);
		verify(likesPostRepository).deleteByUserEmail(USER_EMAIL);
		verify(answersToPollRepository).deleteByUserEmail(USER_EMAIL);
		verify(chatMessageRepository).deleteBySenderEmailOrReceiverEmail(USER_EMAIL, USER_EMAIL);
		verify(commentRepository).updateByUserEmailSetCommenterEmailToNull(USER_EMAIL);
		verify(postRepository).updateByUserEmail(USER_EMAIL);
		verify(eventRepository).updateByOrganizerEmailOrganizerEmailToNull(USER_EMAIL);
		verify(transaction).commit();
	}
}