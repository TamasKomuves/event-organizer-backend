package hu.tamas.university.controller;

import hu.tamas.university.dto.EventTypeDto;
import hu.tamas.university.entity.EventType;
import hu.tamas.university.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/event-types")
public class EventTypeController {

	private final EventTypeRepository eventTypeRepository;

	@Autowired
	public EventTypeController(EventTypeRepository eventTypeRepository) {
		this.eventTypeRepository = eventTypeRepository;
	}

	@GetMapping("/all")
	@ResponseBody
	public List<EventTypeDto> getAllType() {
		List<EventType> eventTypes = eventTypeRepository.findAll();

		return eventTypes.stream().map(EventTypeDto::fromEntity).
				collect(Collectors.toList());
	}
}
