package hu.tamas.university.security;

import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.entity.User;

import java.util.Optional;

public interface UserAuthenticationService {

	/**
	 * Logs in with the given {@code username} and {@code password}.
	 *
	 * @param email
	 * @param password
	 * @return an {@link Optional} of a user when login succeeds
	 */
	Optional<String> login(String email, String password);

	/**
	 * Finds a user by its dao-key.
	 *
	 * @param token user dao key
	 * @return
	 */
	Optional<User> findByToken(String token);

	void changePassword(PasswordChangeDto passwordChangeDto, String loggedInUserEmail);
}
