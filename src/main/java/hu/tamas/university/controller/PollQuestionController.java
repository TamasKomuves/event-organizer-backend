package hu.tamas.university.controller;

import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.repository.PollQuestionRepository;
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
@RequestMapping("/poll-questions")
public class PollQuestionController {

	private final PollQuestionRepository pollQuestionRepository;

	@Autowired
	public PollQuestionController(PollQuestionRepository pollQuestionRepository) {
		this.pollQuestionRepository = pollQuestionRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<PollQuestionDto> getPollQuetionById(@PathVariable int id) {
		PollQuestion pollQuestion = pollQuestionRepository.findPollQuestionById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(PollQuestionDto.fromEntity(pollQuestion), headers, HttpStatus.OK);
	}
}
