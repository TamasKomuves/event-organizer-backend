package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
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
		return Objects.hash(id);
	}
}
