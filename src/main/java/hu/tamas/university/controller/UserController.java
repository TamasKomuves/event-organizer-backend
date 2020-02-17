package hu.tamas.university.controller;

import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import hu.tamas.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserRepository userRepository;
	private final UserAuthenticationService userAuthenticationService;
	private final UserService userService;

	@Autowired
	public UserController(UserRepository userRepository, UserAuthenticationService userAuthenticationService,
			UserService userService) {
		this.userRepository = userRepository;
		this.userAuthenticationService = userAuthenticationService;
		this.userService = userService;
	}

	@GetMapping("/current")
	@ResponseBody
	public UserDto getCurrent(@AuthenticationPrincipal final User user) {
		return UserDto.fromEntity(user);
	}

	@GetMapping("/{email}")
	@ResponseBody
	public UserDto getUserByEmail(@PathVariable String email) {
		User tmpUser = userRepository.findByEmail(email).get();

		return UserDto.fromEntity(tmpUser);
	}

	@GetMapping(value = "/update/{email}/{firstname}/{lastname}")
	@ResponseBody
	public String updateUser(@PathVariable String email, @PathVariable String firstname,
			@PathVariable String lastname) {
		User user = userRepository.findByEmail(email).get();

		user.setFirstName(firstname);
		user.setLastName(lastname);
		userRepository.save(user);

		return "{\"result\":\"success\"}";
	}

	@Transactional
	@DeleteMapping(value = "/delete")
	@ResponseBody
	public String deleteUser(@AuthenticationPrincipal User user) {
		final String result = userService.deleteUser(user.getEmail());

		return "{\"result\":\"" + result + "\"}";
	}

	@GetMapping(value = "/all")
	@ResponseBody
	public List<UserDto> getAllUsers() {
		final List<User> users = userRepository.findAll();

		return users.stream().map(UserDto::fromEntity).collect(Collectors.toList());
	}

	@PutMapping(value = "/change-password")
	@ResponseBody
	public String changePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto,
			@AuthenticationPrincipal User user) {
		userAuthenticationService.changePassword(passwordChangeDto, user.getEmail());
		return "{\"result\":\"success\"}";
	}
}
