package hu.tamas.university.service;

import hu.tamas.university.dto.PollAnswerDto;
import hu.tamas.university.dto.PollQuestionDto;
import hu.tamas.university.dto.creatordto.PollCreatorDto;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.PollAnswer;
import hu.tamas.university.entity.PollQuestion;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.AnswersToPollRepository;
import hu.tamas.university.repository.EventRepository;
import hu.tamas.university.repository.PollAnswerRepository;
import hu.tamas.university.repository.PollQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PollQuestionService {

	private final PollQuestionRepository pollQuestionRepository;
	private final PollAnswerRepository pollAnswerRepository;
	private final EventRepository eventRepository;
	private final AnswersToPollRepository answersToPollRepository;
	private final EntityManager entityManager;

	@Autowired
	public PollQuestionService(PollQuestionRepository pollQuestionRepository,
			PollAnswerRepository pollAnswerRepository, EventRepository eventRepository,
			AnswersToPollRepository answersToPollRepository, EntityManagerFactory entityManagerFactory) {
		this.pollQuestionRepository = pollQuestionRepository;
		this.pollAnswerRepository = pollAnswerRepository;
		this.eventRepository = eventRepository;
		this.answersToPollRepository = answersToPollRepository;
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	public PollQuestionDto getPollQuetionById(final int id) {
		final PollQuestion pollQuestion = pollQuestionRepository.findPollQuestionById(id);
		return PollQuestionDto.fromEntity(pollQuestion);
	}

	public List<Integer> getPollAnswerIdsForQuestion(final int id) {
		final List<PollAnswer> pollAnswers = pollAnswerRepository.findPollAnswersByPollQuestionId(id);
		return pollAnswers.stream() //
				.map(PollAnswer::getId) //
				.collect(Collectors.toList());
	}

	public void createPoll(final PollCreatorDto pollCreatorDto) {
		final PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setText(pollCreatorDto.getQuestionText());
		pollQuestion.setDate(new Timestamp(System.currentTimeMillis()));

		final List<PollAnswer> pollAnswers = pollCreatorDto.getPollAnswers().stream() //
				.map(PollAnswerDto::fromDto) //
				.collect(Collectors.toList());

		for (PollAnswer pollAnswer : pollAnswers) {
			pollQuestion.addPollAnswer(pollAnswer);
		}

		final Event event = eventRepository.findEventById(pollCreatorDto.getEventId());
		event.addPollQuestion(pollQuestion);
		eventRepository.flush();
	}

	public void deletePollQuestion(final int id, final String userEmail) {
		if (!hasPermissionToDeletePollQuestion(id, userEmail)) {
			throw new RuntimeException("no permission");
		}

		final Optional<List<Integer>> pollAnswerIdsOptional = pollAnswerRepository
				.findIdByPollQuestionIds(Collections.singletonList(id));

		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			pollAnswerIdsOptional.ifPresent(answersToPollRepository::deleteByPollAnswerIdIn);
			pollAnswerRepository.deleteByPollQuestionIdIn(Collections.singletonList(id));
			pollQuestionRepository.deleteById(id);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	private boolean hasPermissionToDeletePollQuestion(final int id, final String userEmail) {
		final PollQuestion pollQuestion = pollQuestionRepository.findPollQuestionById(id);
		final User organizer = pollQuestion.getEvent().getOrganizer();
		return organizer != null && userEmail.equals(organizer.getEmail());
	}
}
