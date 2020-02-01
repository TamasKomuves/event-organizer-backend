package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements UserDetails {

	@Id
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ParticipateInEvent> participateInEvents = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Invitation> invitations = new HashSet<>();

	@OneToMany(mappedBy = "organizer", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Event> organizedEvents = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<LikesComment> likesComments = new HashSet<>();

	@OneToMany(mappedBy = "commenter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Comment> comments = new HashSet<>();

	@OneToMany(mappedBy = "poster", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Post> posts = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<LikesPost> likesPosts = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AnswersToPoll> answersToPolls = new HashSet<>();

	@OneToMany(mappedBy = "sender", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ChatMessage> sentChatMessages = new HashSet<>();

	@OneToMany(mappedBy = "receiver", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ChatMessage> receivedChatMessages = new HashSet<>();

	public User() {
	}

	public User(String email, String password, String firstName, String lastName,
			Address address) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		return email.equals(((User) o).email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	public void addEvent(Event event) {
		ParticipateInEvent participateInEvent = new ParticipateInEvent(event, this);
		participateInEvents.add(participateInEvent);
		event.getParticipateInEvents().add(participateInEvent);
	}

	public void removeEvent(Event event) {
		ParticipateInEvent participateInEvent = new ParticipateInEvent(event, this);
		participateInEvents.remove(participateInEvent);
		event.getParticipateInEvents().remove(participateInEvent);
		participateInEvent.setUser(null);
		participateInEvent.setEvent(null);
	}

	public void addLikedComment(Comment comment) {
		LikesComment likesComment = new LikesComment(this, comment);
		likesComments.add(likesComment);
		comment.getLikesComments().add(likesComment);
	}

	public void removeLikedComment(Comment comment) {
		LikesComment likesComment = new LikesComment(this, comment);
		likesComments.remove(likesComment);
		comment.getLikesComments().remove(likesComment);
		likesComment.setComment(null);
		likesComment.setUser(null);
	}

	public void addInvitation(Invitation invitation) {
		invitations.add(invitation);
		invitation.setUser(this);
	}

	public void removeInvitation(Invitation invitation) {
		invitations.remove(invitation);
		invitation.setUser(null);
	}

	public void addOrganizedEvent(Event event) {
		organizedEvents.add(event);
		event.setOrganizer(this);
	}

	public void removeOrganizedEvent(Event event) {
		organizedEvents.remove(event);
		event.setOrganizer(null);
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setCommenter(this);
	}

	public void removeComment(Comment comment) {
		comments.remove(comment);
		comment.setCommenter(null);
	}

	public void addPost(Post post) {
		posts.add(post);
		post.setPoster(this);
	}

	public void removePost(Post post) {
		posts.remove(post);
		post.setPoster(null);
	}

	public void addLikedPost(Post post) {
		LikesPost likesPost = new LikesPost(this, post);
		likesPosts.add(likesPost);
		post.getLikesPosts().add(likesPost);
	}

	public void removeLikedPost(Post post) {
		LikesPost likesPost = new LikesPost(this, post);
		likesPosts.remove(likesPost);
		post.getLikesPosts().remove(likesPost);
		likesPost.setPost(null);
		likesPost.setUser(null);
	}

	public void addAnswerToPoll(PollAnswer pollAnswer) {
		AnswersToPoll answersToPoll = new AnswersToPoll(this, pollAnswer);
		answersToPolls.add(answersToPoll);
		pollAnswer.getAnswersToPolls().add(answersToPoll);
	}

	public void removeAnswerToPoll(PollAnswer pollAnswer) {
		AnswersToPoll answersToPoll = new AnswersToPoll(this, pollAnswer);
		answersToPolls.remove(answersToPoll);
		pollAnswer.getAnswersToPolls().remove(answersToPoll);
		answersToPoll.setPollAnswer(null);
		answersToPoll.setUser(null);
	}

	private void removeSentChatMessage(ChatMessage chatMessage) {
		sentChatMessages.remove(chatMessage);
		chatMessage.setSender(null);
	}

	private void removeReceivedChatMessage(ChatMessage chatMessage) {
		receivedChatMessages.remove(chatMessage);
		chatMessage.setSender(null);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
