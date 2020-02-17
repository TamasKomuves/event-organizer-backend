package hu.tamas.university.service.security;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenAuthenticationServiceTest {

	private static final String USER_EMAIL = "user@gmail.com";
	private static final String PASSWORD = "secretPassword";
	private static final String OLD_PASSWORD = "oldPassword";
	private static final String ENCODED_PASSWORD = "encodedSecretPassword";
	private static final String TOKEN = "944949-9849-49-9";

	@Mock
	private TokenService tokenService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private User user;

	private PasswordChangeDto passwordChangeDto;
	private TokenAuthenticationService tokenAuthenticationService;

	@Before
	public void setUp() {
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
		when(user.getPassword()).thenReturn(ENCODED_PASSWORD);
		when(user.getEmail()).thenReturn(USER_EMAIL);
		when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
		initPasswordChangeDto();

		tokenAuthenticationService = new TokenAuthenticationService(tokenService, userRepository,
				passwordEncoder);
	}

	private void initPasswordChangeDto() {
		passwordChangeDto = new PasswordChangeDto();
		passwordChangeDto.setOldPassword(OLD_PASSWORD);
		passwordChangeDto.setPassword(PASSWORD);
		passwordChangeDto.setPasswordAgain(PASSWORD);
	}

	@Test
	public void testLogin() {
		when(tokenService.expiring(ImmutableMap.of("username", USER_EMAIL))).thenReturn(TOKEN);

		final Optional<String> result = tokenAuthenticationService.login(USER_EMAIL, PASSWORD);

		assertEquals(TOKEN, result.get());
	}

	@Test
	public void testFindByToken() {
		final HashMap<String, String> map = Maps.newHashMap();
		map.put("username", USER_EMAIL);
		when(tokenService.verify(TOKEN)).thenReturn(map);

		final Optional<User> result = tokenAuthenticationService.findByToken(TOKEN);

		assertEquals(user, result.get());
	}

	@Test
	public void testChangePassword() {
		when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
		when(passwordEncoder.matches(OLD_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

		tokenAuthenticationService.changePassword(passwordChangeDto, USER_EMAIL);

		verify(user).setPassword(ENCODED_PASSWORD);
		verify(userRepository).saveAndFlush(user);
	}

	@Test
	public void testChangePassword_NotMatchingPasswords() {
		passwordChangeDto.setPasswordAgain("notMatchingPassword");

		try {
			tokenAuthenticationService.changePassword(passwordChangeDto, USER_EMAIL);
		} catch (RuntimeException e) {
			assertEquals("Not matching passwords", e.getMessage());
		}
	}

	@Test
	public void testChangePassword_InvalidLogin() {
		when(passwordEncoder.matches(OLD_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

		try {
			tokenAuthenticationService.changePassword(passwordChangeDto, USER_EMAIL);
		} catch (RuntimeException e) {
			assertEquals("invalid login and/or password", e.getMessage());
		}
	}
}
