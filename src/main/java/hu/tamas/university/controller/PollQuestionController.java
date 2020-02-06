package hu.tamas.university.controller;

import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.dto.creatordto.PollCreatorDto;
import hu.tamas.university.service.PollQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/poll-questions")
public class PollQuestionController {

	private final PollQuestionService pollQuestionService;

	@Autowired
	public PollQuestionController(PollQuestionService pollQuestionService) {
		this.pollQuestionService = pollQuestionService;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public PollQuestionDto getPollQuetionById(@PathVariable int id) {
		return pollQuestionService.getPollQuetionById(id);
	}

	@GetMapping("/{id}/answerIds")
	@ResponseBody
	public List<Integer> getPollAnswerIdsForQuestion(@PathVariable int id) {
		return pollQuestionService.getPollAnswerIdsForQuestion(id);
	}

	@PostMapping("/createPoll")
	@ResponseBody
	public String createPoll(@RequestBody @Valid PollCreatorDto pollCreatorDto) {
		pollQuestionService.createPoll(pollCreatorDto);
		return "{\"result\":\"success\"}";
	}
}
