package hu.tamas.university.entity;

import javax.persistence.*;

@Entity
@Table(name = "answers_to_poll")
public class AnswersToPoll {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_email")
	private User user;

	@ManyToOne
	@JoinColumn(name = "poll_answer_id")
	private PollAnswer pollAnswer;

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
