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
		List<PollAnswer> pollAnswers = pollAnswerRepository.findAll().stream()
						.filter(pollAnswer -> id == pollAnswer.getPollQuestion().getId()).collect(
										Collectors.toList());

		return pollAnswers.stream().map(PollAnswer::getId).collect(Collectors.toList());
	}

	@PostMapping("/createPoll")
	@ResponseBody
	public String createPoll(@RequestBody @Valid PollCreatorDto pollCreatorDto) {
		Event event = eventRepository.findEventById(pollCreatorDto.getEventId());
		PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setText(pollCreatorDto.getQuestionText());
		pollQuestion.setEvent(event);
		pollQuestion.setDate(new Timestamp(System.currentTimeMillis()));

		PollQuestion savedPollQuestion = pollQuestionRepository.save(pollQuestion);

		List<PollAnswer> pollAnswers = pollCreatorDto.getPollAnswers().stream()
				.map(pollAnswerDto -> PollAnswerDto.fromDto(pollAnswerDto, savedPollQuestion))
				.collect(Collectors.toList());

		for (PollAnswer pollAnswer : pollAnswers) {
			pollAnswerRepository.save(pollAnswer);
		}

		return "{\"result\":\"success\"}";
	}
}
