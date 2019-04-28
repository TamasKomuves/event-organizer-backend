package hu.tamas.university.controller;

import javax.validation.Valid;
import hu.tamas.university.dto.RegistrationDto;
import hu.tamas.university.dto.UserLoginDto;
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
  public String registerUser(@RequestBody @Valid RegistrationDto registrationDto) {
    String result;
    User user = new User();

    if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
      throw new RuntimeException("exists");
    } else {
      Address address = new Address();
      address.setCountry(registrationDto.getCountry());
      address.setCity(registrationDto.getCity());
      address.setStreet(registrationDto.getStreet());
      address.setStreetNumber(registrationDto.getStreetNumber());
      address = addressRepository.save(address);

      user.setEmail(registrationDto.getEmail());
      user.setPassword(registrationDto.getPassword());
      user.setFirstName(registrationDto.getFirstname());
      user.setLastName(registrationDto.getLastname());
      user.setAddress(address);
      userRepository.save(user);

      result = "success";
    }

    return "{\"result\":\"" + result + "\"}";
  }

  @PostMapping("/login")
  @ResponseBody
  public String login(@RequestBody @Valid UserLoginDto userLoginDto) {
    String token = userAuthenticationService.login(userLoginDto.getEmail(), userLoginDto.getPassword())
            .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    return "{\"token\":\"" + token + "\"}";
  }
}
