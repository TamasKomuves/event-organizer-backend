package hu.tamas.university.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@Column(name = "total_cost")
	private int totalCost;

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

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParticipateInEvent> participateInEvents;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> invitations;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PollQuestion> pollQuestions;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxParticipant() {
		return maxParticipant;
	}

	public void setMaxParticipant(int maxParticipant) {
		this.maxParticipant = maxParticipant;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public Timestamp getEventDate() {
		return eventDate;
	}

	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<ParticipateInEvent> getParticipateInEvents() {
		return participateInEvents;
	}

	public void setParticipateInEvents(List<ParticipateInEvent> participateInEvents) {
		this.participateInEvents = participateInEvents;
	}

	public List<Invitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(List<Invitation> invitations) {
		this.invitations = invitations;
	}

	public List<PollQuestion> getPollQuestions() {
		return pollQuestions;
	}

	public void setPollQuestions(List<PollQuestion> pollQuestions) {
		this.pollQuestions = pollQuestions;
	}
}
