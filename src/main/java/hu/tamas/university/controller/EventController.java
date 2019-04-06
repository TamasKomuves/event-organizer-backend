package hu.tamas.university.controller;

import hu.tamas.university.dto.EventDto;
import hu.tamas.university.dto.InvitationDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.*;
import hu.tamas.university.repository.*;
import hu.tamas.university.service.UserService;
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
@RequestMapping("/events")
public class EventController {

	private final String PRIVATE_VISIBILITY = "private";

	private final EventRepository eventRepository;
	private final EventTypeRepository eventTypeRepository;
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final ParticipateInEventRepository participateInEventRepository;
	private final InvitationRepository invitationRepository;
	private final UserService userService;
	private final HttpHeaders headers = new HttpHeaders();

	@Autowired
	public EventController(EventRepository eventRepository, EventTypeRepository eventTypeRepository, AddressRepository addressRepository, UserRepository userRepository, ParticipateInEventRepository participateInEventRepository, InvitationRepository invitationRepository, UserService userService) {
		this.eventRepository = eventRepository;
		this.eventTypeRepository = eventTypeRepository;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.participateInEventRepository = participateInEventRepository;
		this.invitationRepository = invitationRepository;
		this.userService = userService;
		headers.add("Access-Control-Allow-Origin", "*");
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<EventDto> getEventById(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);

		return new ResponseEntity<>(EventDto.fromEntity(event), headers, HttpStatus.OK);
	}

	@GetMapping("")
	public @ResponseBody
	ResponseEntity<List<EventDto>> getAllEvent() {
		List<Event> events = eventRepository.findAll();
		List<EventDto> eventDtos = convertEventsToEventDtos(events);

		return new ResponseEntity<>(eventDtos, headers, HttpStatus.OK);
	}

	private List<EventDto> convertEventsToEventDtos(List<Event> events) {
		return events.stream().filter(this::isCurrentUserHasRightToGetEvent)
				.map(EventDto::fromEntity).collect(Collectors.toList());
	}

	private boolean isCurrentUserHasRightToGetEvent(Event event) {
		return !event.getVisibility().equals(PRIVATE_VISIBILITY) || event.getUsers().contains(userService.getCurrentUser());
	}

	@GetMapping("/type/{type}")
	public @ResponseBody
	ResponseEntity<List<EventDto>> getEventsByType(@PathVariable String type) {
		List<Event> events = eventRepository.findAllByEventTypeType(type);
		List<EventDto> eventDtos = convertEventsToEventDtos(events);

		return new ResponseEntity<>(eventDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/users")
	public @ResponseBody
	ResponseEntity<List<UserDto>> getUsers(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);
		List<UserDto> userDtos = event.getUsers().stream().map(UserDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(userDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/posts")
	public @ResponseBody
	ResponseEntity<List<PostDto>> getAllPost(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);
		List<PostDto> postDtos = event.getPosts().stream().map(PostDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(postDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/create/{name}/{description}/{max_participant}/{visibility}/{total_cost}/{event_date}/{address_id}/{event_type_type}/{organizer_email}")
	public @ResponseBody
	ResponseEntity<String> saveEvent(@PathVariable String name, @PathVariable String description,
	                                 @PathVariable int max_participant, @PathVariable String visibility,
	                                 @PathVariable int total_cost, @PathVariable Timestamp event_date,
	                                 @PathVariable int address_id, @PathVariable String event_type_type,
	                                 @PathVariable String organizer_email) {
		EventType eventType = eventTypeRepository.findEventTypeByType(event_type_type.toLowerCase());

		if (eventType == null) {
			eventType = new EventType();
			eventType.setType(event_type_type.toLowerCase());
			eventType = eventTypeRepository.save(eventType);
		}

		User user = userRepository.findByEmail(organizer_email);
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		event.setMaxParticipant(max_participant);
		event.setVisibility(visibility);
		event.setTotalCost(total_cost);
		event.setEventDate(event_date);
		event.setAddress(addressRepository.findAddressById(address_id));
		event.setEventType(eventType);
		event.setOrganizer(user);

		event = eventRepository.save(event);

		ParticipateInEvent participateInEvent = new ParticipateInEvent();
		participateInEvent.setUser(user);
		participateInEvent.setEvent(event);

		participateInEventRepository.save(participateInEvent);

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/add-user/{userEmail}")
	public @ResponseBody
	ResponseEntity<String> addUserToEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		Event event = eventRepository.findEventById(eventId);

		if (!isEventHasMorePlace(event)) {
			return new ResponseEntity<>("{\"result\":\"no more place\"}", headers, HttpStatus.OK);
		}

		User user = userRepository.findByEmail(userEmail);
		List<ParticipateInEvent> alreadyParticipateInEvent = participateInEventRepository.findByEventAndUser(event,
				user);

		if (!alreadyParticipateInEvent.isEmpty()) {
			return new ResponseEntity<>("{\"result\":\"already added\"}", headers, HttpStatus.OK);
		}

		ParticipateInEvent participateInEvent = createParticipateInEvent(event, user);
		participateInEventRepository.save(participateInEvent);

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	private boolean isEventHasMorePlace(Event event) {
		List<Invitation> invitations = invitationRepository.findByEvent(event).stream()
				.filter(invitation -> invitation.getDecisionDate() == null).collect(Collectors.toList());

		return invitations.size() + event.getUsers().size() < event.getMaxParticipant();
	}

	private ParticipateInEvent createParticipateInEvent(Event event, User user) {
		ParticipateInEvent participateInEvent = new ParticipateInEvent();
		participateInEvent.setEvent(event);
		participateInEvent.setUser(user);
		return participateInEvent;
	}

	@GetMapping("/{eventId}/has-more-place")
	public @ResponseBody
	ResponseEntity<String> eventHasMorePlace(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);
		boolean result = isEventHasMorePlace(event);

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/is-participate/{userEmail}")
	public @ResponseBody
	ResponseEntity<String> isUserParticipateInEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		Event event = eventRepository.findEventById(eventId);
		boolean result = event.getUsers().stream().anyMatch(user -> user.getEmail().equals(userEmail));

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/delete")
	public @ResponseBody
	ResponseEntity<String> deleteEvent(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);

		if (event == null) {
			return new ResponseEntity<>("{\"result\":\"no such event\"}", headers, HttpStatus.OK);
		}

		if (!event.getOrganizer().getEmail().equals(userService.getCurrentUser().getEmail())) {
			return new ResponseEntity<>("{\"result\":\"no permission\"}", headers, HttpStatus.OK);
		}

		invitationRepository.findAll().stream().filter(invitation -> invitation.getEvent().getId() == event.getId())
				.collect(Collectors.toList()).forEach(invitationRepository::delete);
		eventRepository.deleteById(eventId);

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/invitation-offers")
	public @ResponseBody
	ResponseEntity<List<InvitationDto>> getInvitationOffers(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);
		List<InvitationDto> invitationDtos =
				invitationRepository.findByEvent(event).stream()
						.filter(invitation -> invitation.getDecisionDate() == null && invitation.isUserRequested() == 0)
						.map(InvitationDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(invitationDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/invitation-requests")
	public @ResponseBody
	ResponseEntity<List<InvitationDto>> getInvitationRequests(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);
		List<InvitationDto> invitationDtos =
				invitationRepository.findByEvent(event).stream()
						.filter(invitation -> invitation.getDecisionDate() == null && invitation.isUserRequested() == 1)
						.map(InvitationDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(invitationDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/update-info/{name}/{description}/{max_participant}/{total_cost}" +
			"/{event_date}/{visibility}/{address_id}/{event_type_type}")
	public @ResponseBody
	ResponseEntity<String> updateEventInfo(@PathVariable int id, @PathVariable String name,
	                                       @PathVariable String description,
	                                       @PathVariable int max_participant,
	                                       @PathVariable int total_cost, @PathVariable Timestamp event_date,
	                                       @PathVariable String visibility, @PathVariable int address_id,
	                                       @PathVariable String event_type_type) {
		Event event = eventRepository.findEventById(id);
		List<Invitation> invitations = invitationRepository.findByEvent(event).stream()
				.filter(invitation -> invitation.isUserRequested() == 0).collect(Collectors.toList());
		if (event.getUsers().size() + invitations.size() <= max_participant) {
			event.setMaxParticipant(max_participant);
		}

		EventType eventType = eventTypeRepository.findEventTypeByType(event_type_type.toLowerCase());

		if (eventType == null) {
			eventType = new EventType();
			eventType.setType(event_type_type.toLowerCase());
			eventType = eventTypeRepository.save(eventType);
		}

		event.setName(name);
		event.setDescription(description);
		event.setVisibility(visibility);
		event.setTotalCost(total_cost);
		event.setEventDate(event_date);
		event.setAddress(addressRepository.findAddressById(address_id));
		event.setEventType(eventType);

		eventRepository.save(event);

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}
}
