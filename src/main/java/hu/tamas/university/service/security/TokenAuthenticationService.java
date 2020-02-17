package hu.tamas.university.service.security;

import com.google.common.collect.ImmutableMap;
import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
final class TokenAuthenticationService implements UserAuthenticationService {

	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	TokenAuthenticationService(TokenService tokenService, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<String> login(final String email, final String password) {
		return getMatchingUser(email, password)
				.map(user -> tokenService.expiring(ImmutableMap.of("username", email)));
	}

	@Override
	public Optional<User> findByToken(final String token) {
		return Optional
				.of(tokenService.verify(token))
				.map(map -> map.get("username"))
				.flatMap(userRepository::findByEmail);
	}

	@Override
	public void changePassword(PasswordChangeDto passwordChangeDto, String loggedInUserEmail) {
		if (!Objects.equals(passwordChangeDto.getPassword(), passwordChangeDto.getPasswordAgain())) {
			throw new RuntimeException("Not matching passwords");
		}

		final Optional<User> optionalUser = getMatchingUser(loggedInUserEmail, passwordChangeDto.getOldPassword());
		if (!optionalUser.isPresent()) {
			throw new RuntimeException("invalid login and/or password");
		}

		final String encodedPassword = passwordEncoder.encode(passwordChangeDto.getPassword());
		final User user = optionalUser.get();
		user.setPassword(encodedPassword);

		userRepository.saveAndFlush(user);
	}

	private Optional<User> getMatchingUser(final String email, final String password) {
		return userRepository
				.findByEmail(email)
				.filter(user -> passwordEncoder.matches(password, user.getPassword()));
	}
}
