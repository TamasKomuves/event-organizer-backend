package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.ChatMessageDto;
import hu.tamas.university.entity.ChatMessage;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.ChatMessageRepository;
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
public class ChatMessageControllerTest {

	private static final String CURRENT_USER_EMAIL = "current@gmail.com";
	private static final String USER_EMAIL = "user@gmail.com";

	@Mock
	private ChatMessageRepository chatMessageRepository;
	@Mock
	private User currentUser;
	@Mock
	private User user;
	@Mock
	private ChatMessage chatMessage1;
	@Mock
	private ChatMessage chatMessage2;

	private ChatMessageController chatMessageController;

	@Before
	public void setUp() {
		when(currentUser.getEmail()).thenReturn(CURRENT_USER_EMAIL);

		chatMessageController = new ChatMessageController(chatMessageRepository);
	}

	@Test
	public void testGetAllChatMessages() {
		when(chatMessageRepository.findBySenderEmailAndReceiverEmail(CURRENT_USER_EMAIL, USER_EMAIL))
				.thenReturn(Lists.newArrayList(chatMessage1));
		when(chatMessageRepository.findBySenderEmailAndReceiverEmail(USER_EMAIL, CURRENT_USER_EMAIL))
				.thenReturn(Lists.newArrayList(chatMessage2));
		when(chatMessage1.getSender()).thenReturn(currentUser);
		when(chatMessage1.getReceiver()).thenReturn(user);
		when(chatMessage2.getSender()).thenReturn(user);
		when(chatMessage2.getReceiver()).thenReturn(currentUser);

		final List<ChatMessageDto> result = chatMessageController
				.getAllChatMessages(currentUser, USER_EMAIL);

		assertEquals(2, result.size());
	}

	@Test
	public void testNotSeenChatMessageCount() {
		when(chatMessageRepository.countByReceiverEmailAndIsAlreadySeenGroupBySenderEmail(CURRENT_USER_EMAIL))
				.thenReturn(Lists.newArrayList(1L, 2L, 5L, 7L));

		final int result = chatMessageController.notSeenChatMessageCount(currentUser);

		assertEquals(4, result);
	}

	@Test
	public void testMarkAllAsSeenWithPartner() {
		final String result = chatMessageController.markAllAsSeenWithPartner(USER_EMAIL, currentUser);

		assertEquals("{\"result\":\"success\"}", result);
		verify(chatMessageRepository)
				.updateWithPartnerToAlreadySeenByUserEmail(CURRENT_USER_EMAIL, USER_EMAIL);
	}
}
