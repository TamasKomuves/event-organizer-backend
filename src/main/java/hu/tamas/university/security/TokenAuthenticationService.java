package hu.tamas.university.security;

import com.google.common.collect.ImmutableMap;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
final class TokenAuthenticationService implements UserAuthenticationService {

  private final TokenService tokenService;
  private final UserRepository userRepository;

  @Autowired
  TokenAuthenticationService(TokenService tokenService, UserRepository userRepository) {
    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  @Override
  public Optional<String> login(final String username, final String password) {
    return userRepository
            .findByEmail(username)
            .filter(user -> Objects.equals(password, user.getPassword()))
            .map(user -> tokenService.expiring(ImmutableMap.of("username", username)));
  }

  @Override
  public Optional<User> findByToken(final String token) {
    return Optional
            .of(tokenService.verify(token))
            .map(map -> map.get("username"))
            .flatMap(userRepository::findByEmail);
  }

  @Override
  public void logout(final User user) {
    // Nothing to do
  }
}
