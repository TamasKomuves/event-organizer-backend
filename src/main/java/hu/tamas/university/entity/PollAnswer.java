package hu.tamas.university.entity;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "poll_answer")
public class PollAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poll_question_id")
	private PollQuestion pollQuestion;

	@Column(name = "text")
	private String text;

	@OneToMany(mappedBy = "pollAnswer", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<AnswersToPoll> answersToPolls = Sets.newHashSet();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PollAnswer)) {
			return false;
		}
		return id == ((PollAnswer) o).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void addRespondent(User user) {
		AnswersToPoll answersToPoll = new AnswersToPoll(user, this);
		answersToPolls.add(answersToPoll);
		user.getAnswersToPolls().add(answersToPoll);
	}

	public void removeRespondent(User user) {
		AnswersToPoll answersToPoll = new AnswersToPoll(user, this);
		answersToPolls.remove(answersToPoll);
		user.getAnswersToPolls().remove(answersToPoll);
		answersToPoll.setPollAnswer(null);
		answersToPoll.setUser(null);
	}
}