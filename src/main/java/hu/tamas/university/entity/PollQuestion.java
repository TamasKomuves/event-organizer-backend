package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
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

	@OneToMany(mappedBy = "pollQuestion", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<PollAnswer> pollAnswers = new HashSet<>();

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
}
