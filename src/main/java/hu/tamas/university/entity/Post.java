package hu.tamas.university.entity;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poster_email")
	private User poster;

	@Column(name = "post_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp postDate;

	@Column(name = "text")
	private String text;

	@OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Comment> comments = Sets.newHashSet();

	@OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<LikesPost> likesPosts = Sets.newHashSet();

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setPost(this);
	}

	public void removeComment(Comment comment) {
		comments.remove(comment);
		comment.setPost(null);
	}

	public void addLiker(User user) {
		LikesPost likesPost = new LikesPost(user, this);
		likesPosts.add(likesPost);
		user.getLikesPosts().add(likesPost);
	}

	public void removeLiker(User user) {
		LikesPost likesPost = new LikesPost(user, this);
		likesPosts.remove(likesPost);
		user.getLikesPosts().remove(likesPost);
		likesPost.setPost(null);
		likesPost.setUser(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Post)) {
			return false;
		}
		return id == ((Post) o).getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
