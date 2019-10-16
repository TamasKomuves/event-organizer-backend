package hu.tamas.university.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commenter_email")
	private User commenter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "comment_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp commentDate;

	@Column(name = "text")
	private String text;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	List<LikesComment> likesComments;

	public void addLiker(User user) {
		LikesComment likesComment = new LikesComment(user, this);
		likesComments.add(likesComment);
		user.getLikesComments().add(likesComment);
	}

	public void removeLiker(User user) {
		LikesComment likesComment = new LikesComment(user, this);
		likesComments.remove(likesComment);
		user.getLikesComments().remove(likesComment);
		likesComment.setUser(null);
		likesComment.setComment(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Comment)) {
			return false;
		}
		return id == ((Comment) o).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Timestamp getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Timestamp commentDate) {
		this.commentDate = commentDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<LikesComment> getLikesComments() {
		return likesComments;
	}

	public void setLikesComments(List<LikesComment> likesComments) {
		this.likesComments = likesComments;
	}
}
