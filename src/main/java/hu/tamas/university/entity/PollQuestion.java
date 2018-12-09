package hu.tamas.university.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "poll_question")
public class PollQuestion {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;

	@Column(name = "text")
	private String text;

	@Column(name = "poll_question_date")
	private Timestamp pollQuestionDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getPollQuestionDate() {
		return pollQuestionDate;
	}

	public void setPollQuestionDate(Timestamp pollQuestionDate) {
		this.pollQuestionDate = pollQuestionDate;
	}
}
