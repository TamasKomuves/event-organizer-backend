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
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "max_participant")
	private int maxParticipant;

	@Column(name = "visibility")
	private String visibility;

	@Column(name = "event_date")
	private Timestamp eventDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_type_type")
	private EventType eventType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizer_email")
	private User organizer;

	@OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Post> posts = new HashSet<>();

	@OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ParticipateInEvent> participateInEvents = new HashSet<>();

	@OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Invitation> invitations = new HashSet<>();

	@OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<PollQuestion> pollQuestions = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Event)) {
			return false;
		}
		return id == ((Event) o).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void addParticipant(User user) {
		ParticipateInEvent participateInEvent = new ParticipateInEvent(this, user);
		participateInEvents.add(participateInEvent);
		user.getParticipateInEvents().add(participateInEvent);
	}

	public void removeParticipant(User user) {
		ParticipateInEvent participateInEvent = new ParticipateInEvent(this, user);
		participateInEvents.remove(participateInEvent);
		user.getParticipateInEvents().remove(participateInEvent);
		participateInEvent.setUser(null);
		participateInEvent.setEvent(null);
	}

	public void addPost(Post post) {
		posts.add(post);
		post.setEvent(this);
	}

	public void removePost(Post post) {
		posts.remove(post);
		post.setEvent(null);
	}

	public void addInvitation(Invitation invitation) {
		invitations.add(invitation);
		invitation.setEvent(this);
	}

	public void removeInvitation(Invitation invitation) {
		invitations.remove(invitation);
		invitation.setEvent(null);
	}

	public void addPollQuestion(PollQuestion pollQuestion) {
		pollQuestions.add(pollQuestion);
		pollQuestion.setEvent(this);
	}

	public void removePollQuestion(PollQuestion pollQuestion) {
		pollQuestions.remove(pollQuestion);
		pollQuestion.setEvent(null);
	}
}
