package hu.tamas.university.controller;

import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AddressRepository;
import hu.tamas.university.repository.InvitationRepository;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final InvitationRepository invitationRepository;
	private final UserAuthenticationService userAuthenticationService;
	private final HttpHeaders headers = new HttpHeaders();

	@Autowired
	public UserController(AddressRepository addressRepository, UserRepository userRepository, InvitationRepository invitationRepository, UserAuthenticationService userAuthenticationService) {
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.invitationRepository = invitationRepository;
		this.userAuthenticationService = userAuthenticationService;
		headers.add("Access-Control-Allow-Origin", "*");
	}

	@GetMapping("/current")
	@ResponseBody
	public ResponseEntity<UserDto> getCurrent(@AuthenticationPrincipal final User user) {
		UserDto userDto = UserDto.fromEntity(user);
		return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
	}

	@GetMapping("/logout")
	@ResponseBody
	public ResponseEntity<String> logout(@AuthenticationPrincipal final User user) {
		userAuthenticationService.logout(user);
		return new ResponseEntity<>("\"result\":\"success\"", headers, HttpStatus.OK);
	}

	@GetMapping("/{email}")
	public @ResponseBody
	ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
		User tmpUser = userRepository.findByEmail(email).get();

		return new ResponseEntity<>(UserDto.fromEntity(tmpUser), headers, HttpStatus.OK);
	}

//	@GetMapping("/current-user")
//	public @ResponseBody
//	ResponseEntity<UserDto> getCurrentUser() {
//		return new ResponseEntity<>(UserDto.fromEntity(userService.getCurrentUser()), headers, HttpStatus.OK);
//	}

	@GetMapping(value = "/registration/{email}/{password}/{firstname}/{lastname}/{country}/{city}/{street}/{streetNumber}")
	public @ResponseBody
	ResponseEntity<String> registerUser(@PathVariable String email, @PathVariable String password, @PathVariable String firstname, @PathVariable String lastname,
	                                    @PathVariable String country, @PathVariable String city, @PathVariable String street, @PathVariable String streetNumber) {
		String result;
		User user = userRepository.findByEmail(email).get();

		if (user != null) {
			result = "exists";
		} else {
			Address address = new Address();
			address.setCountry(country);
			address.setCity(city);
			address.setStreet(street);
			address.setStreetNumber(streetNumber);
			address = addressRepository.save(address);

			user = new User();
			user.setEmail(email);
			user.setPassword(password);
			user.setFirstName(firstname);
			user.setLastName(lastname);
			user.setAddress(address);
			userRepository.save(user);

			result = "success";
		}

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping(value = "/update/{email}/{firstname}/{lastname}")
	public @ResponseBody
	ResponseEntity<String> updateUser(@PathVariable String email, @PathVariable String firstname, @PathVariable String lastname) {
		String result;
		User user = userRepository.findByEmail(email).get();

		user.setFirstName(firstname);
		user.setLastName(lastname);
		userRepository.save(user);
		result = "success";

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping(value = "/delete/{email}")
	public @ResponseBody
	ResponseEntity<String> deleteUser(@PathVariable String email) {
		String result;
		User user = userRepository.findByEmail(email).get();

		List<Invitation> invitationList = invitationRepository.findByUser(user);
		invitationRepository.deleteAll(invitationList);
		userRepository.delete(user);
		addressRepository.delete(user.getAddress());
		userAuthenticationService.logout(user);
		result = "success";

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}
}
