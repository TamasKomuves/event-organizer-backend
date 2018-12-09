package hu.tamas.university.controller;

import hu.tamas.university.dto.EventDto;
import hu.tamas.university.dto.PostDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.ParticipateInEvent;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

	private final EventRepository eventRepository;
	private final EventTypeRepository eventTypeRepository;
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final ParticipateInEventRepository participateInEventRepository;

	@Autowired
	public EventController(EventRepository eventRepository, EventTypeRepository eventTypeRepository, AddressRepository addressRepository, UserRepository userRepository, ParticipateInEventRepository participateInEventRepository) {
		this.eventRepository = eventRepository;
		this.eventTypeRepository = eventTypeRepository;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.participateInEventRepository = participateInEventRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<EventDto> getEventById(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(EventDto.fromEntity(event), headers, HttpStatus.OK);
	}

	@GetMapping("/type/{type}")
	public @ResponseBody
	ResponseEntity<List<EventDto>> getEventByType(@PathVariable String type) {
		List<Event> events = eventRepository.findAllByEventTypeType(type);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		List<EventDto> eventDtos = events.stream().map(EventDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(eventDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/users")
	public @ResponseBody
	ResponseEntity<List<UserDto>> getUsers(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);
		List<UserDto> userDtos = event.getUsers().stream().map(UserDto::fromEntity).collect(Collectors.toList());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(userDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/{id}/posts")
	public @ResponseBody
	ResponseEntity<List<PostDto>> getAllPost(@PathVariable int id) {
		Event event = eventRepository.findEventById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		List<PostDto> postDtos = event.getPosts().stream().map(PostDto::fromEntity).collect(Collectors.toList());

		return new ResponseEntity<>(postDtos, headers, HttpStatus.OK);
	}

	@GetMapping("/create/{name}/{description}/{max_participant}/{visibility}/{total_cost}/{event_date}/{address_id}/{event_type_type}/{organizer_email}")
	public @ResponseBody
	ResponseEntity<String> saveEvent(@PathVariable String name, @PathVariable String description,
	                                 @PathVariable int max_participant, @PathVariable String visibility,
	                                 @PathVariable int total_cost, @PathVariable Date event_date,
	                                 @PathVariable int address_id, @PathVariable String event_type_type,
	                                 @PathVariable String organizer_email) {
		User user = userRepository.findByEmail(organizer_email);
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		event.setMaxParticipant(max_participant);
		event.setVisibility(visibility);
		event.setTotalCost(total_cost);
		event.setEventDate(event_date);
		event.setAddress(addressRepository.findAddressById(address_id));
		event.setEventType(eventTypeRepository.findEventTypeByType(event_type_type));
		event.setOrganizer(user);

		event = eventRepository.save(event);

		ParticipateInEvent participateInEvent = new ParticipateInEvent();
		participateInEvent.setUser(user);
		participateInEvent.setEvent(event);

		participateInEventRepository.save(participateInEvent);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("")
	public @ResponseBody
	ResponseEntity<List<EventDto>> getAllEvent() {
		List<EventDto> events = eventRepository.findAll().stream().map(EventDto::fromEntity).collect(Collectors.toList());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(events, headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/add-user/{userEmail}")
	public @ResponseBody
	ResponseEntity<String> addUserToEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		ParticipateInEvent participateInEvent = new ParticipateInEvent();
		participateInEvent.setEvent(eventRepository.findEventById(eventId));
		participateInEvent.setUser(userRepository.findByEmail(userEmail));

		ParticipateInEvent result = participateInEventRepository.save(participateInEvent);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\"}", headers, HttpStatus.OK);
	}

	@GetMapping("/{eventId}/is-participate/{userEmail}")
	public @ResponseBody
	ResponseEntity<String> isUserParticipateInEvent(@PathVariable int eventId, @PathVariable String userEmail) {
		Event event = eventRepository.findEventById(eventId);
		boolean result = event.getUsers().stream().anyMatch(user -> user.getEmail().equals(userEmail));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"" + result + "\"}", headers, HttpStatus.OK);
	}
}
