package hu.tamas.university.entity;

import javax.persistence.*;

@Entity
@Table(name = "likes_comment")
public class LikesComment {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_email")
	private User user;

	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comment comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
