package hu.tamas.university.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "invitation")
public class Invitation {

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

	@Column(name = "sent_date")
	private Timestamp sentDate;

	@Column(name = "decision_date")
	private Timestamp decisionDate;

	@Column(name = "is_accepted")
	private int accepted;

	@Column(name = "is_user_requested")
	private int userRequested;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Invitation)) {
			return false;
		}
		return id == ((Invitation) o).getId();
	}

	@Override
	public int hashCode() {
		return 41;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getSentDate() {
		return sentDate;
	}

	public void setSentDate(Timestamp sentDate) {
		this.sentDate = sentDate;
	}

	public Timestamp getDecisionDate() {
		return decisionDate;
	}

	public void setDecisionDate(Timestamp decisionDate) {
		this.decisionDate = decisionDate;
	}

	public int getAccepted() {
		return accepted;
	}

	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}

	public int getUserRequested() {
		return userRequested;
	}

	public void setUserRequested(int userRequested) {
		this.userRequested = userRequested;
	}
}
