package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "participate_in_event")
public class ParticipateInEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_email")
	private User user;

	public ParticipateInEvent() {
	}

	public ParticipateInEvent(Event event, User user) {
		this.event = event;
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ParticipateInEvent)) {
			return false;
		}
		return event == ((ParticipateInEvent) o).event && user == ((ParticipateInEvent) o).user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(event, user);
	}
}
