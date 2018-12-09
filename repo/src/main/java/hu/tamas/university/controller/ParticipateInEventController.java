package hu.tamas.university.controller;

import hu.tamas.university.dto.InvitationDto;
import hu.tamas.university.dto.ParticipateInEventDto;
import hu.tamas.university.entity.ParticipateInEvent;
import hu.tamas.university.repository.ParticipateInEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public @ResponseBody
	ResponseEntity<ParticipateInEventDto> getParticipateInEventById(@PathVariable int id) {
		ParticipateInEvent participateInEvent = participateInEventRepository.findParticipateInEventById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(ParticipateInEventDto.fromEntity(participateInEvent), headers, HttpStatus.OK);
	}
}
