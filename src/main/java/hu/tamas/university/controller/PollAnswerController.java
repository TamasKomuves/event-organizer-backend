package hu.tamas.university.controller;

import hu.tamas.university.dto.AnswersToPollDto;
import hu.tamas.university.dto.PollAnswerDto;
import hu.tamas.university.entity.AnswersToPoll;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AnswersToPollRepository;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.PollQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/poll-answers")
public class PollAnswerController {

	private final PollAnswerRepository pollAnswerRepository;
	private final AnswersToPollRepository answersToPollRepository;
	private final PollQuestionRepository pollQuestionRepository;

	@Autowired
	public PollAnswerController(PollAnswerRepository pollAnswerRepository,
					AnswersToPollRepository answersToPollRepository,
					PollQuestionRepository pollQuestionRepository) {
		this.pollAnswerRepository = pollAnswerRepository;
		this.answersToPollRepository = answersToPollRepository;
		this.pollQuestionRepository = pollQuestionRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PollAnswerDto getPollAnswerById(@PathVariable int id) {
		PollAnswer pollAnswer = pollAnswerRepository.findPollAnswerById(id);

		return PollAnswerDto.fromEntity(pollAnswer);
	}

	@GetMapping("/{id}/votes")
	@ResponseBody
	public List<AnswersToPollDto> getVotesByPollAnswerId(@PathVariable int id) {
		List<AnswersToPoll> answersToPolls = answersToPollRepository.findAnswersToPollByPollAnswerId(id);

		return answersToPolls.stream().map(AnswersToPollDto::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{answerId}/is-already-selected")
	@ResponseBody
	public String isAlreadySelected(@PathVariable int answerId, @AuthenticationPrincipal final User user) {
		AnswersToPoll answersToPoll = answersToPollRepository
						.findAnswersToPollByPollAnswerIdAndUserEmail(answerId, user.getEmail());
		boolean isAlreadySelected = answersToPoll != null;

		return "{\"isAlreadySelected\":\"" + isAlreadySelected + "\"}";
	}

	@PostMapping("/create")
	@ResponseBody
	public PollAnswerDto createPollAnswer(@RequestBody PollAnswerDto pollAnswerDto) {
		PollQuestion pollQuestion = pollQuestionRepository.findPollQuestionById(pollAnswerDto.getPollQuestionId());

		PollAnswer pollAnswer = new PollAnswer();
		pollAnswer.setText(pollAnswerDto.getText());
		pollAnswer.setPollQuestion(pollQuestion);

		PollAnswer createdAnswer = pollAnswerRepository.save(pollAnswer);

		return PollAnswerDto.fromEntity(createdAnswer);
	}
}
