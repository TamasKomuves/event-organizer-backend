package hu.tamas.university.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParticipateInEvent> participateInEvents;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> invitations;

	@OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> organizedEvents;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LikesComment> likesComments;

	@OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	@OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LikesPost> likesPosts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AnswersToPoll> answersToPolls;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> sentChatMessages;

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> receivedChatMessages;

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<Event> getOrganizedEvents() {
		return organizedEvents;
	}

	public void setOrganizedEvents(List<Event> organizedEvents) {
		this.organizedEvents = organizedEvents;
	}

	public List<LikesComment> getLikesComments() {
		return likesComments;
	}

	public void setLikesComments(List<LikesComment> likesComments) {
		this.likesComments = likesComments;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<LikesPost> getLikesPosts() {
		return likesPosts;
	}

	public void setLikesPosts(List<LikesPost> likesPosts) {
		this.likesPosts = likesPosts;
	}

	public List<AnswersToPoll> getAnswersToPolls() {
		return answersToPolls;
	}

	public void setAnswersToPolls(List<AnswersToPoll> answersToPolls) {
		this.answersToPolls = answersToPolls;
	}

	public List<ChatMessage> getSentChatMessages() {
		return sentChatMessages;
	}

	public void setSentChatMessages(List<ChatMessage> sentChatMessages) {
		this.sentChatMessages = sentChatMessages;
	}

	public List<ChatMessage> getReceivedChatMessages() {
		return receivedChatMessages;
	}

	public void setReceivedChatMessages(List<ChatMessage> receivedChatMessages) {
		this.receivedChatMessages = receivedChatMessages;
	}
}
