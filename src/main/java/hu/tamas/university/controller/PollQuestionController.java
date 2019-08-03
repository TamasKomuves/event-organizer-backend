package hu.tamas.university.controller;

import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.PollQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/poll-questions")
public class PollQuestionController {

	private final PollQuestionRepository pollQuestionRepository;
	private final PollAnswerRepository pollAnswerRepository;

	@Autowired
	public PollQuestionController(PollQuestionRepository pollQuestionRepository,
					PollAnswerRepository pollAnswerRepository) {
		this.pollQuestionRepository = pollQuestionRepository;
		this.pollAnswerRepository = pollAnswerRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PollQuestionDto getPollQuetionById(@PathVariable int id) {
		PollQuestion pollQuestion = pollQuestionRepository.findPollQuestionById(id);

		return PollQuestionDto.fromEntity(pollQuestion);
	}

	@GetMapping("/{id}/answerIds")
	@ResponseBody
	public List<Integer> getPollAnswerIdsForQuestion(@PathVariable int id) {
		List<PollAnswer> pollAnswers = pollAnswerRepository.findAll().stream()
						.filter(pollAnswer -> id == pollAnswer.getPollQuestion().getId()).collect(
										Collectors.toList());

		return pollAnswers.stream().map(PollAnswer::getId).collect(Collectors.toList());
	}
}
