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

	@Column(name = "is_activated")
	private int isActivated;

	@Column(name = "activation_token")
	private String activationToken;

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

	@OneToMany(mappedBy = "rater", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<EventRating> eventRatings = new HashSet<>();

	public User() {
		this.isActivated = 0;
	}

	public User(String email, String password, String firstName, String lastName,
			Address address) {
		this();
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

	public void addOrganizedEvent(final Event event) {
		organizedEvents.add(event);
		event.setOrganizer(this);
	}

	public void addComment(final Comment comment) {
		comments.add(comment);
		comment.setCommenter(this);
	}

	public void addPost(final Post post) {
		posts.add(post);
		post.setPoster(this);
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
