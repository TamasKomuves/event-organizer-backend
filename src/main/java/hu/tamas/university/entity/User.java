package hu.tamas.university.entity;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

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
	private Set<ParticipateInEvent> participateInEvents = Sets.newHashSet();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Invitation> invitations = Sets.newHashSet();

	@OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Event> organizedEvents = Sets.newHashSet();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<LikesComment> likesComments = Sets.newHashSet();

	@OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Comment> comments = Sets.newHashSet();

	@OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Post> posts = Sets.newHashSet();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<LikesPost> likesPosts = Sets.newHashSet();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AnswersToPoll> answersToPolls = Sets.newHashSet();

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ChatMessage> sentChatMessages = Sets.newHashSet();

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ChatMessage> receivedChatMessages = Sets.newHashSet();

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

	public Set<ParticipateInEvent> getParticipateInEvents() {
		return participateInEvents;
	}

	public void setParticipateInEvents(Set<ParticipateInEvent> participateInEvents) {
		this.participateInEvents = participateInEvents;
	}

	public Set<Invitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(Set<Invitation> invitations) {
		this.invitations = invitations;
	}

	public Set<Event> getOrganizedEvents() {
		return organizedEvents;
	}

	public void setOrganizedEvents(Set<Event> organizedEvents) {
		this.organizedEvents = organizedEvents;
	}

	public Set<LikesComment> getLikesComments() {
		return likesComments;
	}

	public void setLikesComments(Set<LikesComment> likesComments) {
		this.likesComments = likesComments;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<LikesPost> getLikesPosts() {
		return likesPosts;
	}

	public void setLikesPosts(Set<LikesPost> likesPosts) {
		this.likesPosts = likesPosts;
	}

	public Set<AnswersToPoll> getAnswersToPolls() {
		return answersToPolls;
	}

	public void setAnswersToPolls(Set<AnswersToPoll> answersToPolls) {
		this.answersToPolls = answersToPolls;
	}

	public Set<ChatMessage> getSentChatMessages() {
		return sentChatMessages;
	}

	public void setSentChatMessages(Set<ChatMessage> sentChatMessages) {
		this.sentChatMessages = sentChatMessages;
	}

	public Set<ChatMessage> getReceivedChatMessages() {
		return receivedChatMessages;
	}

	public void setReceivedChatMessages(Set<ChatMessage> receivedChatMessages) {
		this.receivedChatMessages = receivedChatMessages;
	}
}
