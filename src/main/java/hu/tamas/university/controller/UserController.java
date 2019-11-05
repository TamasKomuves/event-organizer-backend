package hu.tamas.university.controller;

import hu.tamas.university.dto.PasswordChangeDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AddressRepository;
import hu.tamas.university.repository.InvitationRepository;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final InvitationRepository invitationRepository;
	private final UserAuthenticationService userAuthenticationService;

	@Autowired
	public UserController(AddressRepository addressRepository, UserRepository userRepository,
			InvitationRepository invitationRepository, UserAuthenticationService userAuthenticationService) {
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.invitationRepository = invitationRepository;
		this.userAuthenticationService = userAuthenticationService;
	}

	@GetMapping("/current")
	@ResponseBody
	public UserDto getCurrent(@AuthenticationPrincipal final User user) {
		return UserDto.fromEntity(user);
	}

	@GetMapping("/logout")
	@ResponseBody
	public String logout(@AuthenticationPrincipal final User user) {
		userAuthenticationService.logout(user);
		return "\"result\":\"success\"";
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
		String result;
		User user = userRepository.findByEmail(email).get();

		user.setFirstName(firstname);
		user.setLastName(lastname);
		userRepository.save(user);
		result = "success";

		return "{\"result\":\"" + result + "\"}";
	}

	@DeleteMapping(value = "/delete/{email}")
	@ResponseBody
	public String deleteUser(@PathVariable String email) {
		String result;
		User user = userRepository.findByEmail(email).get();

		List<Invitation> invitationList = invitationRepository.findByUser(user);
		invitationRepository.deleteAll(invitationList);
		userRepository.delete(user);
		addressRepository.delete(user.getAddress());
		userAuthenticationService.logout(user);
		result = "success";

		return "{\"result\":\"" + result + "\"}";
	}

	@GetMapping(value = "/all")
	@ResponseBody
	public List<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();

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
