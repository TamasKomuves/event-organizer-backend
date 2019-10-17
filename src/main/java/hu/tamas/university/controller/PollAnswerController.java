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
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/poll-answers")
public class PollAnswerController {

	private final PollAnswerRepository pollAnswerRepository;
	private final AnswersToPollRepository answersToPollRepository;
	private final PollQuestionRepository pollQuestionRepository;
	private final UserRepository userRepository;

	@Autowired
	public PollAnswerController(PollAnswerRepository pollAnswerRepository,
			AnswersToPollRepository answersToPollRepository, PollQuestionRepository pollQuestionRepository,
			UserRepository userRepository) {
		this.pollAnswerRepository = pollAnswerRepository;
		this.answersToPollRepository = answersToPollRepository;
		this.pollQuestionRepository = pollQuestionRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PollAnswerDto getPollAnswerById(@PathVariable final int id) {
		final PollAnswer pollAnswer = pollAnswerRepository.findPollAnswerById(id);

		return PollAnswerDto.fromEntity(pollAnswer);
	}

	@GetMapping("/{id}/votes")
	@ResponseBody
	public List<AnswersToPollDto> getVotesByPollAnswerId(@PathVariable final int id) {
		final List<AnswersToPoll> answersToPolls = answersToPollRepository.findAnswersToPollByPollAnswerId(id);

		return AnswersToPollDto.fromEntityList(answersToPolls);
	}

	@GetMapping("/{answerId}/is-already-selected")
	@ResponseBody
	public String isAlreadySelected(@PathVariable final int answerId, @AuthenticationPrincipal final User user) {
		boolean isAlreadySelected = isAlreadyAnswered(answerId, user.getEmail());
		return "{\"isAlreadySelected\":\"" + isAlreadySelected + "\"}";
	}

	@PostMapping("/create")
	@ResponseBody
	public String createPollAnswer(@RequestBody final PollAnswerDto pollAnswerDto) {
		final PollQuestion pollQuestion = pollQuestionRepository
				.findPollQuestionById(pollAnswerDto.getPollQuestionId());
		final PollAnswer pollAnswer = new PollAnswer();

		pollAnswer.setText(pollAnswerDto.getText());
		pollQuestion.addPollAnswer(pollAnswer);

		pollQuestionRepository.flush();

		return "{\"result\":\"success\"}";
	}

	@GetMapping("/{answerId}/add-respondent")
	@ResponseBody
	public String addRespondent(@PathVariable final int answerId, @AuthenticationPrincipal final User authUser) {
		final String email = authUser.getEmail();

		if (isAlreadyAnswered(answerId, email)) {
			return "{\"result\":\"alreadyAnswered\"}";
		}

		final PollAnswer pollAnswer = pollAnswerRepository.findPollAnswerById(answerId);
		final User user = userRepository.findByEmail(email).get();

		pollAnswer.addRespondent(user);
		pollAnswerRepository.flush();
		return "{\"result\":\"success\"}";
	}

	@GetMapping("/{answerId}/remove-respondent")
	@ResponseBody
	public String removeRespondent(@PathVariable final int answerId, @AuthenticationPrincipal final User authUser) {
		final PollAnswer pollAnswer = pollAnswerRepository.findPollAnswerById(answerId);
		final User user = userRepository.findByEmail(authUser.getEmail()).get();

		pollAnswer.removeRespondent(user);
		pollAnswerRepository.flush();
		return "{\"result\":\"success\"}";
	}

	private boolean isAlreadyAnswered(final int answerId, final String userEmail) {
		final Optional<AnswersToPoll> answersToPoll = answersToPollRepository
				.findAnswersToPollByPollAnswerIdAndUserEmail(answerId, userEmail);
		return answersToPoll.isPresent();
	}
}
