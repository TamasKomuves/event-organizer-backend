package hu.tamas.university.controller;

import hu.tamas.university.dto.EventDto;
import hu.tamas.university.dto.InvitationDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.*;
import hu.tamas.university.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

	@Autowired
	public EventController(EventRepository eventRepository, EventTypeRepository eventTypeRepository, AddressRepository addressRepository, UserRepository userRepository, ParticipateInEventRepository participateInEventRepository, InvitationRepository invitationRepository) {
		this.eventRepository = eventRepository;
		this.eventTypeRepository = eventTypeRepository;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.participateInEventRepository = participateInEventRepository;
		this.invitationRepository = invitationRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public EventDto getEventById(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);

		return EventDto.fromEntity(event);
	}

	@GetMapping("")
	@ResponseBody
	public List<EventDto> getAllEvent(@AuthenticationPrincipal final User user) {
		List<Event> events = eventRepository.findAll();

		return convertEventsToEventDtos(events, user);
	}

	private List<EventDto> convertEventsToEventDtos(List<Event> events, User user) {
		return events.stream().filter(event -> isCurrentUserHasRightToGetEvent(event, user))
				.map(EventDto::fromEntity).collect(Collectors.toList());
	}

	private boolean isCurrentUserHasRightToGetEvent(Event event, User user) {
		return !event.getVisibility().equals(PRIVATE_VISIBILITY) || event.getUsers().contains(user);
	}

	@GetMapping("/type/{type}")
	@ResponseBody
	public List<EventDto> getEventsByType(@AuthenticationPrincipal final User user, @PathVariable String type) {
		List<Event> events = eventRepository.findAllByEventTypeType(type);

		return convertEventsToEventDtos(events, user);
	}

	@GetMapping("/{id}/users")
	@ResponseBody
	public List<UserDto> getUsers(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);

		return event.getUsers().stream().map(UserDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{id}/posts")
	@ResponseBody
	public List<PostDto> getAllPost(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);

		return event.getPosts().stream().map(PostDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/create/{name}/{description}/{max_participant}/{visibility}/{total_cost}/{event_date}/{address_id}/{event_type_type}/{organizer_email}")
	@ResponseBody
	public String saveEvent(@PathVariable String name, @PathVariable String description,
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

		User user = userRepository.findByEmail(organizer_email).get();
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

		return "{\"result\":\"success\"}";
	}

	@GetMapping("/{eventId}/add-user/{userEmail}")
	@ResponseBody
	public String addUserToEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		Event event = eventRepository.findEventById(eventId);

		if (!isEventHasMorePlace(event)) {
			return "{\"result\":\"no more place\"}";
		}

		User user = userRepository.findByEmail(userEmail).get();
		List<ParticipateInEvent> alreadyParticipateInEvent = participateInEventRepository.findByEventAndUser(event,
				user);

		if (!alreadyParticipateInEvent.isEmpty()) {
			return "{\"result\":\"already added\"}";
		}

		ParticipateInEvent participateInEvent = createParticipateInEvent(event, user);
		participateInEventRepository.save(participateInEvent);

		return "{\"result\":\"success\"}";
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
	@ResponseBody
	public String eventHasMorePlace(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);
		boolean result = isEventHasMorePlace(event);

		return "{\"result\":\"" + result + "\"}";
	}

	@GetMapping("/{eventId}/is-participate/{userEmail}")
	@ResponseBody
	public String isUserParticipateInEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		Event event = eventRepository.findEventById(eventId);
		boolean result = event.getUsers().stream().anyMatch(user -> user.getEmail().equals(userEmail));

		return "{\"result\":\"" + result + "\"}";
	}

	@GetMapping("/{eventId}/delete")
	@ResponseBody
	public String deleteEvent(@AuthenticationPrincipal final User user, @PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);

		if (event == null) {
			return "{\"result\":\"no such event\"}";
		}

		if (!event.getOrganizer().getEmail().equals(user.getEmail())) {
			return "{\"result\":\"no permission\"}";
		}

		invitationRepository.findAll().stream().filter(invitation -> invitation.getEvent().getId() == event.getId())
				.collect(Collectors.toList()).forEach(invitationRepository::delete);
		eventRepository.deleteById(eventId);

		return "{\"result\":\"success\"}";
	}

	@GetMapping("/{eventId}/invitation-offers")
	@ResponseBody
	public List<InvitationDto> getInvitationOffers(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);

		return invitationRepository.findByEvent(event).stream()
						.filter(invitation -> invitation.getDecisionDate() == null && invitation.isUserRequested() == 0)
						.map(InvitationDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{eventId}/invitation-requests")
	@ResponseBody
	public List<InvitationDto> getInvitationRequests(@PathVariable int eventId) {
		Event event = eventRepository.findEventById(eventId);
		List<InvitationDto> invitationDtos =
				invitationRepository.findByEvent(event).stream()
						.filter(invitation -> invitation.getDecisionDate() == null && invitation.isUserRequested() == 1)
						.map(InvitationDto::fromEntity).collect(Collectors.toList());

		return invitationDtos;
	}

	@GetMapping("/{id}/update-info/{name}/{description}/{max_participant}/{total_cost}" +
			"/{event_date}/{visibility}/{address_id}/{event_type_type}")
	@ResponseBody
	public String updateEventInfo(@PathVariable int id, @PathVariable String name,
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

		return "{\"result\":\"success\"}";
	}
}
