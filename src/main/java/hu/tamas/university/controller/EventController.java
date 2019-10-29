package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.*;
import hu.tamas.university.dto.creatordto.EventCreatorDto;
import hu.tamas.university.entity.*;
import hu.tamas.university.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

	private final String PRIVATE_VISIBILITY = "private";

	private final EventRepository eventRepository;
	private final EventTypeRepository eventTypeRepository;
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final ParticipateInEventRepository participateInEventRepository;
	private final InvitationRepository invitationRepository;
	private final PollQuestionRepository pollQuestionRepository;
	private final PostRepository postRepository;

	@Autowired
	public EventController(EventRepository eventRepository, EventTypeRepository eventTypeRepository,
			AddressRepository addressRepository, UserRepository userRepository,
			ParticipateInEventRepository participateInEventRepository, InvitationRepository invitationRepository,
			PollQuestionRepository pollQuestionRepository, PostRepository postRepository) {
		this.eventRepository = eventRepository;
		this.eventTypeRepository = eventTypeRepository;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.participateInEventRepository = participateInEventRepository;
		this.invitationRepository = invitationRepository;
		this.pollQuestionRepository = pollQuestionRepository;
		this.postRepository = postRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public EventDto getEventById(@PathVariable int id, @AuthenticationPrincipal final User user) {
		final Event event = eventRepository.findEventById(id);

		return EventDto.fromEntity(event);
	}

	@GetMapping("/all")
	@ResponseBody
	public List<EventDto> getAllEvent(@AuthenticationPrincipal final User user) {
		final List<Event> events = eventRepository.findAll();

		return convertEventsToEventDtos(events, user);
	}

	private List<EventDto> convertEventsToEventDtos(List<Event> events, User user) {
		return events.stream().filter(event -> isCurrentUserHasRightToGetEvent(event, user))
				.map(EventDto::fromEntity).collect(Collectors.toList());
	}

	private boolean isCurrentUserHasRightToGetEvent(Event event, User user) {
		return !event.getVisibility().equals(PRIVATE_VISIBILITY) || isParticipate(user, event);
	}

	@GetMapping("/type/{type}")
	@ResponseBody
	public List<EventDto> getEventsByType(@AuthenticationPrincipal final User user, @PathVariable String type) {
		final List<Event> events = eventRepository.findAllByEventTypeType(type);

		return convertEventsToEventDtos(events, user);
	}

	@GetMapping("/{id}/users")
	@ResponseBody
	public List<UserDto> getUsers(@PathVariable int id, @AuthenticationPrincipal final User user) {
		final Event event = eventRepository.findEventById(id);
		final List<User> participants = event.getParticipateInEvents().stream()
				.map(ParticipateInEvent::getUser).collect(Collectors.toList());

		return participants.stream().map(UserDto::fromEntity).collect(Collectors.toList());
	}

	@PostMapping("/create")
	@ResponseBody
	public String createEvent(@RequestBody @Valid EventCreatorDto eventCreatorDto, @AuthenticationPrincipal User user) {
		final String eventTypeLowerCase = eventCreatorDto.getEventType().toLowerCase();
		final EventType eventType = eventTypeRepository.findByType(eventTypeLowerCase)
				.orElse(new EventType(eventTypeLowerCase));
		final Address address = addressRepository.findAddressById(eventCreatorDto.getAddressId());
		final User creator = userRepository.findByEmail(user.getEmail()).get();
		final Event event = EventCreatorDto.fromDto(eventCreatorDto, address, eventType, creator);

		event.addParticipant(creator);

		eventRepository.saveAndFlush(event);

		return "{\"result\":\"success\"}";
	}

	@GetMapping("/{eventId}/add-user/{userEmail}")
	@ResponseBody
	public String addUserToEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		final Event event = eventRepository.findEventById(eventId);
		if (!isEventHasMorePlace(event)) {
			return "{\"result\":\"no more place\"}";
		}

		final boolean isAlreadyParticipate = participateInEventRepository.findByEventIdAndUserEmail(eventId, userEmail)
				.isPresent();
		if (isAlreadyParticipate) {
			return "{\"result\":\"already added\"}";
		}

		final User user = userRepository.findByEmail(userEmail).orElse(null);
		event.addParticipant(user);

		eventRepository.flush();

		return "{\"result\":\"success\"}";
	}

	private boolean isParticipate(User user, Event event) {
		final List<User> participants = event.getParticipateInEvents().stream().map(ParticipateInEvent::getUser)
				.collect(Collectors.toList());
		return participants.stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()));
	}

	private boolean isEventHasMorePlace(Event event) {
		return event.getInvitations().size() + event.getParticipateInEvents().size() < event.getMaxParticipant();
	}

	@GetMapping("/{eventId}/has-more-place")
	@ResponseBody
	public String eventHasMorePlace(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);
		boolean result = isEventHasMorePlace(event);

		return "{\"result\":\"" + result + "\"}";
	}

	@GetMapping("/{eventId}/is-participate/{userEmail}")
	@ResponseBody
	public String isUserParticipateInEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		final boolean isParticipate = participateInEventRepository.findByEventIdAndUserEmail(eventId, userEmail)
				.isPresent();

		return "{\"result\":\"" + isParticipate + "\"}";
	}

	@DeleteMapping("/delete/{eventId}")
	@ResponseBody
	public String deleteEvent(@AuthenticationPrincipal final User user, @PathVariable int eventId) {
		final Event event = eventRepository.findEventById(eventId);

		if (event == null) {
			return "{\"result\":\"no such event\"}";
		}

		if (!event.getOrganizer().getEmail().equals(user.getEmail())) {
			return "{\"result\":\"no permission\"}";
		}

		eventRepository.deleteById(eventId);

		return "{\"result\":\"success\"}";
	}

	@GetMapping("/{eventId}/invitation-offers")
	@ResponseBody
	public List<InvitationDto> getInvitationOffers(@PathVariable int eventId) {
		final List<Invitation> invitations = invitationRepository.findByEventId(eventId).orElse(Lists.newArrayList());
		return invitations.stream().filter(this::isInvitationOffer).map(InvitationDto::fromEntity)
				.collect(Collectors.toList());
	}

	private boolean isInvitationOffer(Invitation invitation) {
		return invitation.getDecisionDate() == null && invitation.getUserRequested() == 0;
	}

	@GetMapping("/{eventId}/invitation-requests")
	@ResponseBody
	public List<InvitationDto> getInvitationRequests(@PathVariable int eventId) {
		final List<Invitation> invitations = invitationRepository.findByEventId(eventId).orElse(Lists.newArrayList());
		return invitations.stream().filter(this::isInvitationRequest).map(InvitationDto::fromEntity)
				.collect(Collectors.toList());
	}

	private boolean isInvitationRequest(Invitation invitation) {
		return invitation.getDecisionDate() == null && invitation.getUserRequested() == 1;
	}

	@PutMapping("/update-info/{id}")
	@ResponseBody
	public String updateEventInfo(@PathVariable int id, @RequestBody EventCreatorDto eventCreatorDto) {
		final Event event = eventRepository.findEventById(id);

		EventCreatorDto.updateInfoFromDto(event, eventCreatorDto);

		final int newMaxParticipant = eventCreatorDto.getMaxParticipant();
		if (isNewMaxParticipantHigher(event, id, newMaxParticipant)) {
			event.setMaxParticipant(newMaxParticipant);
		}

		final String eventTypeLowerCase = eventCreatorDto.getEventType().toLowerCase();
		final EventType eventType = eventTypeRepository.findByType(eventTypeLowerCase)
				.orElse(new EventType(eventTypeLowerCase));
		if (!eventType.equals(event.getEventType())) {
			eventType.addEvent(event);
		}

		eventTypeRepository.saveAndFlush(eventType);

		return "{\"result\":\"success\"}";
	}

	private boolean isNewMaxParticipantHigher(Event event, int eventId, int newMaxParticipant) {
		final Optional<List<Invitation>> invitations = invitationRepository.findByEventId(eventId);
		long invitationCount = 0;

		if (invitations.isPresent()) {
			invitationCount = invitations.get().stream().filter(invitation -> invitation.getUserRequested() == 0)
					.count();
		}

		return event.getParticipateInEvents().size() + invitationCount < newMaxParticipant;
	}

	@GetMapping("/{id}/polls")
	@ResponseBody
	public List<PollQuestionDto> getAllPolls(@PathVariable int id) {
		final List<PollQuestion> pollQuestions = pollQuestionRepository.findAllByEventId(id);
		return pollQuestions.stream().map(PollQuestionDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{id}/news")
	@ResponseBody
	public List<NewsDto> getAllNews(@PathVariable int id) {
		final List<PollQuestionDto> pollQuestionDtos = pollQuestionRepository.findAllByEventId(id).stream()
				.map(PollQuestionDto::fromEntity).collect(Collectors.toList());
		final List<NewsDto> newsDtos = postRepository.findAllByEventId(id).stream().map(PostDto::fromEntity)
				.collect(Collectors.toList());

		newsDtos.addAll(pollQuestionDtos);

		return newsDtos;
	}
}
