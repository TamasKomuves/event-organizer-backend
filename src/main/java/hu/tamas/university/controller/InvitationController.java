package hu.tamas.university.controller;

import hu.tamas.university.dto.InvitationDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.Invitation;
import hu.tamas.university.entity.ParticipateInEvent;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.InvitationRepository;
import hu.tamas.university.repository.ParticipateInEventRepository;
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
	private final ParticipateInEventRepository participateInEventRepository;
	private final HttpHeaders headers = new HttpHeaders();

	@Autowired
	public InvitationController(InvitationRepository invitationRepository, UserRepository userRepository, EventRepository eventRepository, ParticipateInEventRepository participateInEventRepository) {
		this.invitationRepository = invitationRepository;
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.participateInEventRepository = participateInEventRepository;
		headers.add("Access-Control-Allow-Origin", "*");
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<InvitationDto> getInvitationById(@PathVariable int id) {
		Invitation invitation = invitationRepository.findInvitationById(id);

		return new ResponseEntity<>(InvitationDto.fromEntity(invitation), headers, HttpStatus.OK);
	}

	@GetMapping("/create/{eventId}/{userEmail}/{isUserRequested}")
	public @ResponseBody
	ResponseEntity<String> createInvitation(@PathVariable int eventId,
	                                        @PathVariable String userEmail, @PathVariable int isUserRequested) {
		Event event = eventRepository.findEventById(eventId);

		if (!isEventHasMorePlace(event)) {
			return new ResponseEntity<>("{\"result\":\"no more place\"}", headers, HttpStatus.OK);
		}

		User user = userRepository.findByEmail(userEmail);

		List<ParticipateInEvent> alreadyParticipateInEvent = participateInEventRepository.findByEventAndUser(event,
				user);

		if (!alreadyParticipateInEvent.isEmpty()) {
			return new ResponseEntity<>("{\"result\":\"already participate\"}", headers, HttpStatus.OK);
		}

		List<Invitation> alreadyInvited = invitationRepository.findByEventAndUser(event, user);

		if (!alreadyInvited.isEmpty()) {
			return new ResponseEntity<>("{\"result\":\"already invited\"}", headers, HttpStatus.OK);
		}

		Invitation invitation = createInvitation(event, user, isUserRequested);

		invitationRepository.save(invitation);

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	private boolean isEventHasMorePlace(Event event) {
		List<Invitation> invitations = invitationRepository.findByEvent(event).stream()
				.filter(invitation -> invitation.getDecisionDate() == null).collect(Collectors.toList());

		return invitations.size() + event.getUsers().size() < event.getMaxParticipant();
	}

	private Invitation createInvitation(Event event, User user, int isUserRequested) {
		Invitation invitation = new Invitation();
		invitation.setEvent(event);
		invitation.setUser(user);
		invitation.setUserRequested(isUserRequested);
		invitation.setAccepted(0);
		invitation.setSentDate(new Timestamp(System.currentTimeMillis()));
		return invitation;
	}

	@GetMapping("/{id}/answer/{isAccepted}")
	public @ResponseBody
	ResponseEntity<String> answerToInvitation(@PathVariable int id,
	                                          @PathVariable int isAccepted) {
		Invitation invitation = invitationRepository.findInvitationById(id);
		invitation.setAccepted(isAccepted);
		invitation.setDecisionDate(new Timestamp(System.currentTimeMillis()));

		invitationRepository.save(invitation);

		if (isAccepted == 1) {
			ParticipateInEvent participateInEvent = new ParticipateInEvent();
			participateInEvent.setEvent(invitation.getEvent());
			participateInEvent.setUser(invitation.getUser());

			participateInEventRepository.save(participateInEvent);
		}

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/is-invited/{eventId}/{userEmail}")
	public @ResponseBody
	ResponseEntity<String> isAlreadyInvited(@PathVariable int eventId, @PathVariable String userEmail) {
		boolean isAlreadySent = invitationRepository.findAll().stream()
				.anyMatch(invitation -> invitation.getEvent().getId() == eventId &&
						invitation.getUser().getEmail().equals(userEmail) && invitation.getDecisionDate() == null);

		return new ResponseEntity<>("{\"result\":\"" + isAlreadySent + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/users/{userEmail}")
	public @ResponseBody
	ResponseEntity<List<InvitationDto>> getInvitationsForUser(@PathVariable String userEmail) {
		List<InvitationDto> invitationDtos = invitationRepository.findAll().stream()
						.filter(invitation ->
										invitation.getUser() != null && invitation.getUser().getEmail().equals(userEmail))
						.map(InvitationDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(invitationDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/delete")
	public @ResponseBody
	ResponseEntity<String> deleteInvitation(@PathVariable int id) {
		invitationRepository.deleteById(id);
		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}
}
