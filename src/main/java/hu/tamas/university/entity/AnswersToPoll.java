package hu.tamas.university.entity;

import javax.persistence.*;
import java.util.Objects;

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
		return id == ((AnswersToPoll) o).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PollAnswer getPollAnswer() {
		return pollAnswer;
	}

	public void setPollAnswer(PollAnswer pollAnswer) {
		this.pollAnswer = pollAnswer;
	}
}
