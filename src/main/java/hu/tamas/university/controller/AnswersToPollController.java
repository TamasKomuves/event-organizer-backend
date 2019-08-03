package hu.tamas.university.controller;

import hu.tamas.university.dto.AnswersToPollDto;
import hu.tamas.university.entity.AnswersToPoll;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AnswersToPollRepository;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/answers-to-polls")
public class AnswersToPollController {

	private final AnswersToPollRepository answersToPollRepository;
	private final UserRepository userRepository;
	private final PollAnswerRepository pollAnswerRepository;

	@Autowired
	public AnswersToPollController(AnswersToPollRepository answersToPollRepository,
					UserRepository userRepository, PollAnswerRepository pollAnswerRepository) {
		this.answersToPollRepository = answersToPollRepository;
		this.userRepository = userRepository;
		this.pollAnswerRepository = pollAnswerRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public AnswersToPollDto getAnswersToPollById(@PathVariable int id) {
		AnswersToPoll answersToPoll = answersToPollRepository.findAnswersToPollById(id);

		return AnswersToPollDto.fromEntity(answersToPoll);
	}

	@PostMapping("/create")
	@ResponseBody
	public String createAnswersToPoll(@RequestBody @Valid AnswersToPollDto answersToPollDto,
					@AuthenticationPrincipal final User user) {
		PollAnswer answer = pollAnswerRepository.findPollAnswerById(answersToPollDto.getPollAnswerId());

		AnswersToPoll answersToPoll = new AnswersToPoll();
		answersToPoll.setId(answersToPollDto.getId());
		answersToPoll.setUser(user);
		answersToPoll.setPollAnswer(answer);

		answersToPollRepository.save(answersToPoll);

		return "{\"result\":\"success\"}";
	}
}
