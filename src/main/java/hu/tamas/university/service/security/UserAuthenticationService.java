package hu.tamas.university.service.security;

import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.entity.User;

import java.util.Optional;

public interface UserAuthenticationService {

	Optional<String> login(String email, String password);

	Optional<User> findByToken(String token);

	void changePassword(PasswordChangeDto passwordChangeDto, String loggedInUserEmail);
}
