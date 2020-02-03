package hu.tamas.university.controller;

import com.google.common.collect.Lists;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
	public InvitationController(InvitationRepository invitationRepository, UserRepository userRepository,
			EventRepository eventRepository, ParticipateInEventRepository participateInEventRepository) {
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

	@PostMapping("/create")
	@ResponseBody
	public InvitationDto createInvitation(@RequestBody @Valid InvitationDto invitationDto) {
		final Event event = eventRepository.findEventById(invitationDto.getEventId());
		final User user = userRepository.findByEmail(invitationDto.getUserEmail()).get();

		if (!isEventHasMorePlace(event)) {
			throw new RuntimeException("No more place");
		}

		if (isAlreadyParticipateInEvent(invitationDto)) {
			throw new RuntimeException("Already participate");
		}

		if (isAlreadyInvited(invitationDto)) {
			throw new RuntimeException("Already invited");
		}

		final Invitation invitation = createInvitation(event, user, invitationDto.getUserRequested());

		return InvitationDto.fromEntity(invitationRepository.save(invitation));
	}

	private boolean isEventHasMorePlace(Event event) {
		List<Invitation> invitations = invitationRepository.findByEventId(event.getId())
				.orElse(Lists.newArrayList());
		invitations = invitations.stream().filter(invitation -> invitation.getDecisionDate() == null)
				.collect(Collectors.toList());

		return invitations.size() + event.getParticipateInEvents().size() < event.getMaxParticipant();
	}

	private boolean isAlreadyParticipateInEvent(InvitationDto invitationDto) {
		return participateInEventRepository
				.findByEventIdAndUserEmail(invitationDto.getEventId(), invitationDto.getUserEmail())
				.isPresent();
	}

	private boolean isAlreadyInvited(final InvitationDto invitationDto) {
		return invitationRepository
				.findByEventIdAndUserEmailAndDecisionDateIsNull(invitationDto.getEventId(),
						invitationDto.getUserEmail()).isPresent();
	}

	private Invitation createInvitation(Event event, User user, int isUserRequested) {
		final Invitation invitation = new Invitation();
		invitation.setEvent(event);
		invitation.setUser(user);
		invitation.setUserRequested(isUserRequested);
		invitation.setAccepted(0);
		invitation.setSentDate(new Timestamp(System.currentTimeMillis()));
		invitation.setIsAlreadySeen(0);
		return invitation;
	}

	@GetMapping("/{id}/answer/{isAccepted}")
	@ResponseBody
	public InvitationDto answerToInvitation(@PathVariable int id,
			@PathVariable int isAccepted) {
		final Invitation invitation = invitationRepository.findInvitationById(id);
		invitation.setAccepted(isAccepted);
		invitation.setDecisionDate(new Timestamp(System.currentTimeMillis()));

		final Invitation invitationToReturn = invitationRepository.save(invitation);

		if (isAccepted == 1) {
			ParticipateInEvent participateInEvent = new ParticipateInEvent();
			participateInEvent.setEvent(invitation.getEvent());
			participateInEvent.setUser(invitation.getUser());

			participateInEventRepository.save(participateInEvent);
		}

		return InvitationDto.fromEntity(invitationToReturn);
	}

	@GetMapping("/is-invited/{eventId}/{userEmail}")
	@ResponseBody
	public String isAlreadyInvited(@PathVariable int eventId, @PathVariable String userEmail) {
		boolean isAlreadySent = invitationRepository
				.findByEventIdAndUserEmailAndDecisionDateIsNull(eventId, userEmail)
				.isPresent();

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

	@GetMapping("/not-seen")
	@ResponseBody
	public Long countNotSeenMessages(@AuthenticationPrincipal User user) {
		return invitationRepository.countByUserEmailAndIsAlreadySeen(user.getEmail(), 0);
	}

	@PutMapping("/mark-all-as-seen")
	@ResponseBody
	public String markAllAsSeen(@AuthenticationPrincipal User user) {
		invitationRepository.updateAllToAlreadySeenByUserEmail(user.getEmail());
		return "{\"result\":\"success\"}";
	}
}
