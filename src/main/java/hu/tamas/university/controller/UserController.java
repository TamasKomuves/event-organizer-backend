package hu.tamas.university.controller;

import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AddressRepository;
import hu.tamas.university.repository.InvitationRepository;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final InvitationRepository invitationRepository;
	private final HttpHeaders headers = new HttpHeaders();

	@Autowired
	public UserController(UserService userService, AddressRepository addressRepository, UserRepository userRepository, InvitationRepository invitationRepository) {
		this.userService = userService;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.invitationRepository = invitationRepository;
		headers.add("Access-Control-Allow-Origin", "*");
	}

	@GetMapping("/{email}")
	public @ResponseBody
	ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
		User tmpUser = userService.findByEmail(email);

		return new ResponseEntity<>(UserDto.fromEntity(tmpUser), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/login/{email}/{password}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> login(@PathVariable String email, @PathVariable String password) {
		User user = userService.findByEmail(email);
		String result;

		if (user != null && user.getPassword().equals(password)) {
			userService.setCurrentUser(user);
			result = "success";
		} else {
			result = "failed";
		}

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/current-user")
	public @ResponseBody
	ResponseEntity<UserDto> getCurrentUser() {
		return new ResponseEntity<>(UserDto.fromEntity(userService.getCurrentUser()), headers, HttpStatus.OK);
	}

	@GetMapping("/logout")
	public @ResponseBody
	ResponseEntity<String> logout() {
		userService.setCurrentUser(null);

		return new ResponseEntity<>("{\"result\":\"logout\"}", headers, HttpStatus.OK);
	}

	@GetMapping(value = "/registration/{email}/{password}/{firstname}/{lastname}/{country}/{city}/{street}/{streetNumber}")
	public @ResponseBody
	ResponseEntity<String> registerUser(@PathVariable String email, @PathVariable String password, @PathVariable String firstname, @PathVariable String lastname,
	                                    @PathVariable String country, @PathVariable String city, @PathVariable String street, @PathVariable String streetNumber) {
		String result;
		User user = userService.findByEmail(email);

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
			userService.saveUser(user);

			result = "success";
		}

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping(value = "/update/{email}/{firstname}/{lastname}")
	public @ResponseBody
	ResponseEntity<String> updateUser(@PathVariable String email, @PathVariable String firstname, @PathVariable String lastname) {
		String result;
		User user = userService.findByEmail(email);

		if (user == null) {
			result = "no such user";
		} else {
			user.setFirstName(firstname);
			user.setLastName(lastname);
			userService.saveUser(user);
			result = "success";
		}

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping(value = "/delete/{email}")
	public @ResponseBody
	ResponseEntity<String> deleteUser(@PathVariable String email) {
		String result;
		User user = userService.findByEmail(email);

		if (user == null) {
			result = "no such user";
		} else {
			List<Invitation> invitationList = invitationRepository.findByUser(user);
			invitationRepository.deleteAll(invitationList);
			userRepository.delete(user);
			addressRepository.delete(user.getAddress());
			userService.setCurrentUser(null);
			result = "success";
		}

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}
}
