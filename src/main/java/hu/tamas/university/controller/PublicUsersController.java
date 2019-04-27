package hu.tamas.university.controller;

import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AddressRepository;
import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public/users")
final class PublicUsersController {

  private final UserAuthenticationService userAuthenticationService;
  private final UserRepository userRepository;
  private final AddressRepository addressRepository;

  @Autowired
  public PublicUsersController(UserAuthenticationService userAuthenticationService, UserRepository userRepository, AddressRepository addressRepository) {
    this.userAuthenticationService = userAuthenticationService;
    this.userRepository = userRepository;
    this.addressRepository = addressRepository;
  }

  @PostMapping("/registration")
  @ResponseBody
  public String registerUser(@RequestParam String email, @RequestParam String password,
                             @RequestParam String firstname, @RequestParam String lastname,
                             @RequestParam String country, @RequestParam String city, @RequestParam String street, @RequestParam String streetNumber) {
    String result;
    User user = new User();

    if (userRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException("exists");
    } else {
      Address address = new Address();
      address.setCountry(country);
      address.setCity(city);
      address.setStreet(street);
      address.setStreetNumber(streetNumber);
      address = addressRepository.save(address);

      user.setEmail(email);
      user.setPassword(password);
      user.setFirstName(firstname);
      user.setLastName(lastname);
      user.setAddress(address);
      userRepository.save(user);

      result = "success";
    }

    return "{\"result\":\"" + result + "\"}";
  }

  @GetMapping("/login/{username}/{password}")
  @ResponseBody
  public String login(@PathVariable("username") String username, @PathVariable("password") String password) {
    String token = userAuthenticationService.login(username, password)
            .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    return "{\"token\":\"" + token + "\"}";
  }
}
