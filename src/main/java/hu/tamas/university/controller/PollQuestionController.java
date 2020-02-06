package hu.tamas.university.controller;

import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.dto.creatordto.PollCreatorDto;
import hu.tamas.university.entity.User;
import hu.tamas.university.service.PollQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/poll-questions")
public class PollQuestionController {

	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";

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
		return RESULT_SUCCESS;
	}

	@DeleteMapping("{id}/delete")
	@ResponseBody
	public String deletePollQuestion(@PathVariable int id, @AuthenticationPrincipal User user) {
		pollQuestionService.deletePollQuestion(id, user.getEmail());
		return RESULT_SUCCESS;
	}
}
