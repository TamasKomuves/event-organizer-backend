package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "likes_comment")
public class LikesComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_email")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	public LikesComment() {
	}

	public LikesComment(User user, Comment comment) {
		this.user = user;
		this.comment = comment;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LikesComment)) {
			return false;
		}
		return user == ((LikesComment) o).user && comment == ((LikesComment) o).comment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, comment);
	}
}
