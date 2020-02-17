package hu.tamas.university.config.security;

import hu.tamas.university.service.security.UserAuthenticationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static lombok.AccessLevel.PUBLIC;

@Component
@AllArgsConstructor(access = PUBLIC)
final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@NonNull
	private final UserAuthenticationService userAuthenticationService;

	@Override
	protected void additionalAuthenticationChecks(final UserDetails d,
			final UsernamePasswordAuthenticationToken auth) {
		// Nothing to do
	}

	@Override
	protected UserDetails retrieveUser(final String username,
			final UsernamePasswordAuthenticationToken authentication) {
		final Object token = authentication.getCredentials();
		return Optional
				.ofNullable(token)
				.map(String::valueOf)
				.flatMap(userAuthenticationService::findByToken)
				.orElseThrow(
						() -> new UsernameNotFoundException(
								"Cannot find user with authentication token=" + token));
	}
}
