package hu.tamas.university.controller;

import hu.tamas.university.dto.AnswersToPollDto;
import hu.tamas.university.entity.AnswersToPoll;
import hu.tamas.university.repository.AnswersToPollRepository;
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
@RequestMapping("/answers-to-polls")
public class AnswersToPollController {

	private final AnswersToPollRepository answersToPollRepository;

	@Autowired
	public AnswersToPollController(AnswersToPollRepository answersToPollRepository) {
		this.answersToPollRepository = answersToPollRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<AnswersToPollDto> getAnswersToPollById(@PathVariable int id) {
		AnswersToPoll answersToPoll = answersToPollRepository.findAnswersToPollById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(AnswersToPollDto.fromEntity(answersToPoll), headers, HttpStatus.OK);
	}
}
