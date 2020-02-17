package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import hu.tamas.university.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	private static final String CURRENT_USER_EMAIL = "current@gmail.com";
	private static final String USER_EMAIL = "user@gmail.com";
	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";;

	@Mock
	private UserRepository userRepository;
	@Mock
	private UserAuthenticationService userAuthenticationService;
	@Mock
	private UserService userService;
	@Mock
	private User currentUser;
	@Mock
	private User user;
	@Mock
	private PasswordChangeDto passwordChangeDto;

	@Captor
	private ArgumentCaptor<User> userCaptor;

	private UserController userController;

	@Before
	public void setUp() {
		when(currentUser.getEmail()).thenReturn(CURRENT_USER_EMAIL);
		when(userRepository.findByEmail(CURRENT_USER_EMAIL)).thenReturn(Optional.of(currentUser));
		when(user.getEmail()).thenReturn(USER_EMAIL);
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

		userController = new UserController(userRepository, userAuthenticationService, userService);
	}

	@Test
	public void testGetCurrent() {
		final UserDto result = userController.getUserByEmail(CURRENT_USER_EMAIL);

		assertEquals(CURRENT_USER_EMAIL, result.getEmail());
	}

	@Test
	public void testUpdateUser() {
		final String result = userController.updateUser(USER_EMAIL, "NEW_FIRSTNAME", "NEW_LASTNAME");

		assertEquals(RESULT_SUCCESS, result);
		verify(userRepository).save(userCaptor.capture());
		final User capturedUser = userCaptor.getValue();
		assertEquals(USER_EMAIL, capturedUser.getEmail());
		verify(capturedUser).setFirstName("NEW_FIRSTNAME");
		verify(capturedUser).setLastName("NEW_LASTNAME");
	}

	@Test
	public void testDeleteUser() {
		when(userService.deleteUser(CURRENT_USER_EMAIL)).thenReturn("success");

		final String result = userController.deleteUser(currentUser);

		assertEquals(RESULT_SUCCESS, result);
	}

	@Test
	public void testGetAllUsers() {
		when(userRepository.findAll()).thenReturn(Lists.newArrayList(user, currentUser));

		final List<UserDto> result = userController.getAllUsers();

		assertEquals(2, result.size());
		assertEquals(USER_EMAIL, result.get(0).getEmail());
		assertEquals(CURRENT_USER_EMAIL, result.get(1).getEmail());
	}

	@Test
	public void testChangePassword() {
		final String result = userController.changePassword(passwordChangeDto, currentUser);

		assertEquals(RESULT_SUCCESS, result);
		verify(userAuthenticationService).changePassword(passwordChangeDto, CURRENT_USER_EMAIL);
	}
}