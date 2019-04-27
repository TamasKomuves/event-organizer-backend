package hu.tamas.university.controller;

import hu.tamas.university.repository.UserRepository;
import hu.tamas.university.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public/users")
final class PublicUsersController {

  private final UserAuthenticationService userAuthenticationService;
  private final UserRepository userRepository;
  private final HttpHeaders headers = new HttpHeaders();

  @Autowired
  public PublicUsersController(UserAuthenticationService userAuthenticationService, UserRepository userRepository) {
    this.userAuthenticationService = userAuthenticationService;
    this.userRepository = userRepository;
    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  }

  @PostMapping("/register")
  @ResponseBody
  ResponseEntity<String> register(
          @RequestParam("username") final String username,
          @RequestParam("password") final String password) {
    return new ResponseEntity<>("\"result\":\"asd\"", headers, HttpStatus.OK);
  }

  @GetMapping("/login/{username}/{password}")
  @ResponseBody
  public ResponseEntity<String> login(
          @PathVariable("username") String username,
          @PathVariable("password") String password) {
    String token = userAuthenticationService
            .login(username, password)
            .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    return new ResponseEntity<>("{\"token\":\"" + token + "\"}", headers, HttpStatus.OK);
  }
}
