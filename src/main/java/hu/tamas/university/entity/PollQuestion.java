package hu.tamas.university.entity;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "poll_question")
public class PollQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@Column(name = "text")
	private String text;

	@Column(name = "poll_question_date")
	private Timestamp date;

	@OneToMany(mappedBy = "pollQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PollAnswer> pollAnswers = Sets.newHashSet();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PollQuestion)) {
			return false;
		}
		return id == ((PollQuestion) o).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void addPollAnswer(PollAnswer pollAnswer) {
		pollAnswers.add(pollAnswer);
		pollAnswer.setPollQuestion(this);
	}

	public void removePollAnswer(PollAnswer pollAnswer) {
		pollAnswers.remove(pollAnswer);
		pollAnswer.setPollQuestion(null);
	}

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

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Set<PollAnswer> getPollAnswers() {
		return pollAnswers;
	}

	public void setPollAnswers(Set<PollAnswer> pollAnswers) {
		this.pollAnswers = pollAnswers;
	}
}
