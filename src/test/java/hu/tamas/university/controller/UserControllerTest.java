package hu.tamas.university.controller;

import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import hu.tamas.university.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	private static final String CURRENT_USER_EMAIL = "current@gmail.com";

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

	private UserController userController;

	@Before
	public void setUp() {
		userController = new UserController(userRepository, userAuthenticationService, userService);
	}

	@Test
	public void testGetCurrent() {

	}
}