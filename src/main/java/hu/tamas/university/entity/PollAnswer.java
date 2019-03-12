package hu.tamas.university.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "poll_answer")
public class PollAnswer {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "poll_question_id")
	private PollQuestion pollQuestion;

	@Column(name = "text")
	private String text;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PollQuestion getPollQuestion() {
		return pollQuestion;
	}

	public void setPollQuestion(PollQuestion pollQuestion) {
		this.pollQuestion = pollQuestion;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}