package hu.tamas.university.controller;

import hu.tamas.university.dto.RegistrationDto;
import hu.tamas.university.dto.UserLoginDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PublicUsersControllerTest {

	private static final String USER_EMAIL = "user@gmail.com";
	private static final String PASSWORD = "secretPassword";
	private static final String COUNTRY = "country";
	private static final String ENCODED_PASSWORD = "encodedSecretPassword";
	private static final String REQUEST_URL = "requestUrl";
	private static final String TOKEN = "1651ASDASCCEG4646xfsdf6449f";
	private static final StringBuffer URL_STRING_BUFFER = new StringBuffer(REQUEST_URL);

	@Mock
	private UserAuthenticationService userAuthenticationService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JavaMailSender javaMailSender;
	@Mock
	private RegistrationDto registrationDto;
	@Mock
	private HttpServletRequest request;
	@Mock
	private UserLoginDto userLoginDto;
	@Mock
	private User user;

	@Captor
	private ArgumentCaptor<User> userCaptor;

	private PublicUsersController publicUsersController;

	@Before
	public void setUp() {
		when(registrationDto.getEmail()).thenReturn(USER_EMAIL);
		when(registrationDto.getPassword()).thenReturn(PASSWORD);
		when(registrationDto.getCountry()).thenReturn(COUNTRY);
		when(userLoginDto.getEmail()).thenReturn(USER_EMAIL);
		when(userLoginDto.getPassword()).thenReturn(PASSWORD);

		publicUsersController = new PublicUsersController(userAuthenticationService, userRepository,
				passwordEncoder, javaMailSender);
	}

	@Test
	public void testRegisterUser() {
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
		when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
		when(request.getRequestURL()).thenReturn(URL_STRING_BUFFER);

		final String result = publicUsersController.registerUser(registrationDto, request);

		assertEquals("{\"result\":\"success\"}", result);
		verify(userRepository).saveAndFlush(userCaptor.capture());
		verify(javaMailSender).send(any(SimpleMailMessage.class));
		final User userResult = userCaptor.getValue();
		assertEquals(USER_EMAIL, userResult.getEmail());
		assertEquals(ENCODED_PASSWORD, userResult.getPassword());
		assertEquals(COUNTRY, userResult.getAddress().getCountry());
	}

	@Test
	public void testRegisterUser_Exists() {
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mock(User.class)));

		try {
			publicUsersController.registerUser(registrationDto, request);
		} catch (RuntimeException e) {
			assertEquals("exists", e.getMessage());
		}
	}

	@Test
	public void testLogin() {
		when(userAuthenticationService.login(USER_EMAIL, PASSWORD)).thenReturn(Optional.of(TOKEN));
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
		when(user.getIsActivated()).thenReturn(1);

		final String result = publicUsersController.login(userLoginDto);

		assertEquals("{\"token\":\"" + TOKEN + "\"}", result);
	}

	@Test
	public void testLogin_WrongPassword() {
		when(userAuthenticationService.login(USER_EMAIL, PASSWORD)).thenReturn(Optional.empty());

		try {
			publicUsersController.login(userLoginDto);
		} catch (RuntimeException e) {
			assertEquals("invalid login and/or password", e.getMessage());
		}
	}

	@Test
	public void testLogin_NotActivated() {
		when(userAuthenticationService.login(USER_EMAIL, PASSWORD)).thenReturn(Optional.of(TOKEN));
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
		when(user.getIsActivated()).thenReturn(0);

		try {
			publicUsersController.login(userLoginDto);
		} catch (RuntimeException e) {
			assertEquals("not activated", e.getMessage());
		}
	}

	@Test
	public void testActivateUser() {
		when(userRepository.findByActivationToken(TOKEN)).thenReturn(Optional.of(user));

		final String result = publicUsersController.activateUser(TOKEN);

		assertEquals("Successful activation, now you can log in!", result);
		verify(user).setIsActivated(1);
		verify(userRepository).saveAndFlush(user);
	}
}