package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "answers_to_poll")
public class AnswersToPoll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_email")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poll_answer_id")
	private PollAnswer pollAnswer;

	public AnswersToPoll() {
	}

	public AnswersToPoll(User user, PollAnswer pollAnswer) {
		this.user = user;
		this.pollAnswer = pollAnswer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AnswersToPoll)) {
			return false;
		}
		return Objects.equals(user, ((AnswersToPoll) o).user) && Objects
				.equals(pollAnswer, ((AnswersToPoll) o).pollAnswer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, pollAnswer);
	}
}
