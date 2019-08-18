package hu.tamas.university.entity;

import javax.persistence.*;

@Entity
@Table(name = "participate_in_event")
public class ParticipateInEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne
	@JoinColumn(name="event_id")
	private Event event;

	@ManyToOne
	@JoinColumn(name="user_email")
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
