package hu.tamas.university.controller;

import hu.tamas.university.dto.PollAnswerDto;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.repository.PollAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/poll-answers")
public class PollAnswerController {

	private final PollAnswerRepository pollAnswerRepository;

	@Autowired
	public PollAnswerController(PollAnswerRepository pollAnswerRepository) {
		this.pollAnswerRepository = pollAnswerRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PollAnswerDto getPollAnswerById(@PathVariable int id) {
		PollAnswer pollAnswer = pollAnswerRepository.findPollAnswerById(id);

		return PollAnswerDto.fromEntity(pollAnswer);
	}
}
