package hu.tamas.university.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "event_rating")
public class EventRating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rater_email")
	private User rater;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@Column(name = "rating")
	private double rating;

	public EventRating(User rater, Event event) {
		this.rater = rater;
		this.event = event;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
