package hu.tamas.university.controller;

import hu.tamas.university.dto.InvitationDto;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.InvitationRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/invitations")
public class InvitationController {

	private final InvitationRepository invitationRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;

	@Autowired
	public InvitationController(InvitationRepository invitationRepository, UserRepository userRepository, EventRepository eventRepository) {
		this.invitationRepository = invitationRepository;
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<InvitationDto> getInvitationById(@PathVariable int id) {
		Invitation invitation = invitationRepository.findInvitationById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(InvitationDto.fromEntity(invitation), headers, HttpStatus.OK);
	}

	@GetMapping("/create/{eventId}/{userEmail}/{isUserRequested}")
	public @ResponseBody
	ResponseEntity<String> createInvitation(@PathVariable int eventId,
	                                        @PathVariable String userEmail, @PathVariable int isUserRequested) {
		Invitation invitation = new Invitation();
		invitation.setEvent(eventRepository.findEventById(eventId));
		invitation.setUser(userRepository.findByEmail(userEmail));
		invitation.setUserRequested(isUserRequested);
		invitation.setAccepted(0);
		invitation.setSentDate(new Timestamp(System.currentTimeMillis()));

		invitationRepository.save(invitation);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/answer/{isAccepted}")
	public @ResponseBody
	ResponseEntity<String> answerToInvitation(@PathVariable int id,
	                                        @PathVariable int isAccepted) {
		Invitation invitation = invitationRepository.findInvitationById(id);
		invitation.setAccepted(isAccepted);
		invitation.setDecisionDate(new Timestamp(System.currentTimeMillis()));

		invitationRepository.save(invitation);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/is-invited/{eventId}/{userEmail}")
	public @ResponseBody
	ResponseEntity<String> isAlreadyInvited(@PathVariable int eventId, @PathVariable String userEmail) {
		boolean isAlreadySent = invitationRepository.findAll().stream().anyMatch(invitation -> invitation.getEvent().getId() == eventId &&
				invitation.getUser().getEmail().equals(userEmail));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"" + isAlreadySent + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/users/{userEmail}")
	public @ResponseBody
	ResponseEntity<List<InvitationDto>> getInvitationsForUser(@PathVariable String userEmail) {
		List<InvitationDto> invitationDtos = invitationRepository.findAll().stream().
				filter(invitation -> invitation.getUser().getEmail().equals(userEmail)).map(InvitationDto::fromEntity).collect(Collectors.toList());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(invitationDtos, headers, HttpStatus.OK);
	}
}
