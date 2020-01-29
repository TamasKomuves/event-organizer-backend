package hu.tamas.university.controller;

import hu.tamas.university.dto.RegistrationDto;
import hu.tamas.university.dto.UserLoginDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/public/users")
final class PublicUsersController {

	private final UserAuthenticationService userAuthenticationService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public PublicUsersController(UserAuthenticationService userAuthenticationService,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userAuthenticationService = userAuthenticationService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/registration")
	@ResponseBody
	public String registerUser(@RequestBody @Valid RegistrationDto registrationDto) {
		if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
			throw new RuntimeException("exists");
		}
		final Address address = new Address(registrationDto.getCountry(), registrationDto.getCity(),
				registrationDto.getStreet(), registrationDto.getStreetNumber());

		final String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());
		final User user = new User(registrationDto.getEmail(), encodedPassword,
				registrationDto.getFirstname(),
				registrationDto.getLastname(), address);
		userRepository.saveAndFlush(user);

		return "{\"result\":\"success\"}";
	}

	@PostMapping("/login")
	@ResponseBody
	public String login(@RequestBody @Valid UserLoginDto userLoginDto) {
		final String token = userAuthenticationService
				.login(userLoginDto.getEmail(), userLoginDto.getPassword())
				.orElseThrow(() -> new RuntimeException("invalid login and/or password"));
		return "{\"token\":\"" + token + "\"}";
	}
}
