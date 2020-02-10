package hu.tamas.university.controller;

import hu.tamas.university.dto.RegistrationDto;
import hu.tamas.university.dto.UserLoginDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/public/users")
final class PublicUsersController {

	private final UserAuthenticationService userAuthenticationService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;

	@Autowired
	public PublicUsersController(UserAuthenticationService userAuthenticationService,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
		this.userAuthenticationService = userAuthenticationService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.javaMailSender = javaMailSender;
	}

	@PostMapping("/registration")
	@ResponseBody
	public String registerUser(@RequestBody @Valid RegistrationDto registrationDto,
			HttpServletRequest request) {
		if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
			throw new RuntimeException("exists");
		}
		final Address address = new Address(registrationDto.getCountry(), registrationDto.getCity(),
				registrationDto.getStreet(), registrationDto.getStreetNumber());

		final String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());
		final User user = new User(registrationDto.getEmail(), encodedPassword,
				registrationDto.getFirstname(), registrationDto.getLastname(), address);
		final String token = UUID.randomUUID().toString();
		user.setActivationToken(token);
		userRepository.saveAndFlush(user);

		sendConfirmationEmail(registrationDto.getEmail(), token, request.getRequestURL().toString());

		return "{\"result\":\"success\"}";
	}

	private void sendConfirmationEmail(final String userEmail, final String token, final String baseUrl) {
		final SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(userEmail);
		msg.setSubject("Event organizer - registration confirmation");
		final String url = baseUrl + "/activate-user/" + token;
		msg.setText("To finish your registration please click on the following link: " + url);
		javaMailSender.send(msg);
	}

	@PostMapping("/login")
	@ResponseBody
	public String login(@RequestBody @Valid UserLoginDto userLoginDto) {
		final String userEmail = userLoginDto.getEmail();
		final String token = userAuthenticationService
				.login(userEmail, userLoginDto.getPassword())
				.orElseThrow(() -> new RuntimeException("invalid login and/or password"));

		if (!isActivated(userEmail)) {
			throw new RuntimeException("not activated");
		}

		return "{\"token\":\"" + token + "\"}";
	}

	private boolean isActivated(final String userEmail) {
		final User user = userRepository.findByEmail(userEmail).get();
		return user.getIsActivated() == 1;
	}

	@GetMapping("/registration/activate-user/{token}")
	@ResponseBody
	public String activateUser(@PathVariable String token) {
		final User user = userRepository.findByActivationToken(token).get();
		user.setIsActivated(1);
		userRepository.saveAndFlush(user);
		return "Successful activation, now you can log in!";
	}
}
