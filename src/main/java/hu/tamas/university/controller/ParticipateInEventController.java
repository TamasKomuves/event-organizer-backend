package hu.tamas.university.controller;

import hu.tamas.university.dto.ParticipateInEventDto;
import hu.tamas.university.entity.ParticipateInEvent;
import hu.tamas.university.repository.ParticipateInEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/participate-in-events")
public class ParticipateInEventController {

	private final ParticipateInEventRepository participateInEventRepository;

	@Autowired
	public ParticipateInEventController(ParticipateInEventRepository participateInEventRepository) {
		this.participateInEventRepository = participateInEventRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ParticipateInEventDto getParticipateInEventById(@PathVariable int id) {
		ParticipateInEvent participateInEvent = participateInEventRepository.findParticipateInEventById(id);

		return ParticipateInEventDto.fromEntity(participateInEvent);
	}
}
