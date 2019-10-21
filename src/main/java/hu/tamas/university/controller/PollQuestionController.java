package hu.tamas.university.controller;

import hu.tamas.university.dto.PollAnswerDto;
import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.dto.creatordto.PollCreatorDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.PollQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/poll-questions")
public class PollQuestionController {

	private final PollQuestionRepository pollQuestionRepository;
	private final PollAnswerRepository pollAnswerRepository;
	private final EventRepository eventRepository;

	@Autowired
	public PollQuestionController(PollQuestionRepository pollQuestionRepository,
			PollAnswerRepository pollAnswerRepository, EventRepository eventRepository) {
		this.pollQuestionRepository = pollQuestionRepository;
		this.pollAnswerRepository = pollAnswerRepository;
		this.eventRepository = eventRepository;
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
		final List<PollAnswer> pollAnswers = pollAnswerRepository.findPollAnswersByPollQuestionId(id);

		return pollAnswers.stream().map(PollAnswer::getId).collect(Collectors.toList());
	}

	@PostMapping("/createPoll")
	@ResponseBody
	public String createPoll(@RequestBody @Valid PollCreatorDto pollCreatorDto) {
		final PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setText(pollCreatorDto.getQuestionText());
		pollQuestion.setDate(new Timestamp(System.currentTimeMillis()));

		final List<PollAnswer> pollAnswers = pollCreatorDto.getPollAnswers().stream().map(PollAnswerDto::fromDto)
				.collect(Collectors.toList());

		for (PollAnswer pollAnswer : pollAnswers) {
			pollQuestion.addPollAnswer(pollAnswer);
		}

		final Event event = eventRepository.findEventById(pollCreatorDto.getEventId());
		event.addPollQuestion(pollQuestion);
		eventRepository.flush();

		return "{\"result\":\"success\"}";
	}
}
