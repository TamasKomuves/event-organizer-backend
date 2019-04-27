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

	@Autowired
	public InvitationController(InvitationRepository invitationRepository, UserRepository userRepository, EventRepository eventRepository, ParticipateInEventRepository participateInEventRepository) {
		this.invitationRepository = invitationRepository;
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.participateInEventRepository = participateInEventRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public InvitationDto getInvitationById(@PathVariable int id) {
		Invitation invitation = invitationRepository.findInvitationById(id);

		return InvitationDto.fromEntity(invitation);
	}

	@GetMapping("/create/{eventId}/{userEmail}/{isUserRequested}")
	@ResponseBody
	public String createInvitation(@PathVariable int eventId,
	                                        @PathVariable String userEmail, @PathVariable int isUserRequested) {
		Event event = eventRepository.findEventById(eventId);

		if (!isEventHasMorePlace(event)) {
			return "{\"result\":\"no more place\"}";
		}

		User user = userRepository.findByEmail(userEmail).get();

		List<ParticipateInEvent> alreadyParticipateInEvent = participateInEventRepository.findByEventAndUser(event,
				user);

		if (!alreadyParticipateInEvent.isEmpty()) {
			return "{\"result\":\"already participate\"}";
		}

		List<Invitation> alreadyInvited = invitationRepository.findByEventAndUser(event, user);

		if (!alreadyInvited.isEmpty()) {
			return "{\"result\":\"already invited\"}";
		}

		Invitation invitation = createInvitation(event, user, isUserRequested);

		invitationRepository.save(invitation);

		return "{\"result\":\"success\"}";
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
	@ResponseBody
	public String answerToInvitation(@PathVariable int id,
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

		return "{\"result\":\"success\"}";
	}

	@GetMapping("/is-invited/{eventId}/{userEmail}")
	@ResponseBody
	public String isAlreadyInvited(@PathVariable int eventId, @PathVariable String userEmail) {
		boolean isAlreadySent = invitationRepository.findAll().stream()
				.anyMatch(invitation -> invitation.getEvent().getId() == eventId &&
						invitation.getUser().getEmail().equals(userEmail) && invitation.getDecisionDate() == null);

		return "{\"result\":\"" + isAlreadySent + "\"}";
	}

	@GetMapping("/users/{userEmail}")
	@ResponseBody
	public List<InvitationDto> getInvitationsForUser(@PathVariable String userEmail) {

		return invitationRepository.findAll().stream()
						.filter(invitation ->
										invitation.getUser() != null && invitation.getUser().getEmail().equals(userEmail))
						.map(InvitationDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{id}/delete")
	@ResponseBody
	public String deleteInvitation(@PathVariable int id) {
		invitationRepository.deleteById(id);
		return "{\"result\":\"success\"}";
	}
}
