package hu.tamas.university.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "invitation")
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne
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
