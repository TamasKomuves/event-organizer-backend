package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "likes_post")
public class LikesPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_email")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	public LikesPost() {
	}

	public LikesPost(User user, Post post) {
		this.user = user;
		this.post = post;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LikesPost)) {
			return false;
		}
		return user == ((LikesPost) o).user && post == ((LikesPost) o).post;
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, post);
	}
}
