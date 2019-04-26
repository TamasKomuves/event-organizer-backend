package hu.tamas.university.service;

import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

	private final UserRepository userRepository;

	private User currentUser;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email).get();
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}


	public User getCurrentUser() {
		if (currentUser != null) {
			User user = findByEmail(currentUser.getEmail());
			setCurrentUser(user);
		}
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
