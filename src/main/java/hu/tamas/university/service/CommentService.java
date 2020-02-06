package hu.tamas.university.service;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.CommentDto;
import hu.tamas.university.dto.UserDto;
import hu.tamas.university.entity.Comment;
import hu.tamas.university.entity.LikesComment;
import hu.tamas.university.entity.Post;
import hu.tamas.university.entity.User;
import hu.tamas.university.repository.CommentRepository;
import hu.tamas.university.repository.LikesCommentRepository;
import hu.tamas.university.repository.PostRepository;
import hu.tamas.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final LikesCommentRepository likesCommentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final EntityManager entityManager;

	@Autowired
	public CommentService(CommentRepository commentRepository,
			LikesCommentRepository likesCommentRepository,
			PostRepository postRepository,
			UserRepository userRepository,
			EntityManagerFactory entityManagerFactory) {
		this.commentRepository = commentRepository;
		this.likesCommentRepository = likesCommentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		entityManager = entityManagerFactory.createEntityManager();
	}

	public CommentDto getCommentById(final int id) {
		final Comment comment = commentRepository.findCommentById(id);
		return CommentDto.fromEntity(comment);
	}

	public List<UserDto> getLikers(final int id) {
		final List<LikesComment> likesComments = likesCommentRepository.findByCommentId(id)
				.orElse(Lists.newArrayList());

		return likesComments.stream().map(likesComment -> UserDto.fromEntity(likesComment.getUser()))
				.collect(Collectors.toList());
	}

	public void createComment(final CommentDto commentDto, final String userEmail) {
		final Post post = postRepository.findPostById(commentDto.getPostId());
		final User commenter = userRepository.findByEmail(userEmail).get();
		final Comment comment = new Comment();

		comment.setText(commentDto.getText());
		comment.setCommentDate(new Timestamp(System.currentTimeMillis()));
		post.addComment(comment);
		commenter.addComment(comment);

		commentRepository.saveAndFlush(comment);
	}

	public boolean isLikedAlready(final int id, final String email) {
		final Optional<LikesComment> likesComment = likesCommentRepository
				.findByCommentIdAndUserEmail(id, email);
		return likesComment.isPresent();
	}

	public void addLiker(final int id, final String userEmail) {
		final Comment comment = commentRepository.findCommentById(id);
		final User liker = userRepository.findByEmail(userEmail).get();

		comment.addLiker(liker);
		commentRepository.flush();
	}

	public void removeLiker(final int id, final String userEmail) {
		final Comment comment = commentRepository.findCommentById(id);
		final User liker = userRepository.findByEmail(userEmail).get();

		comment.removeLiker(liker);
		commentRepository.saveAndFlush(comment);
	}

	public void deleteComment(final int id, final String userEmail) {
		if (!hasPermissionToDeleteComment(id, userEmail)) {
			throw new RuntimeException("no permission");
		}

		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			likesCommentRepository.deleteByCommentIdIn(Collections.singletonList(id));
			commentRepository.deleteById(id);
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteCommentsOfPosts(final List<Integer> postIds) {
		if (postIds == null || postIds.isEmpty()) {
			return;
		}

		final List<Integer> commentIds = commentRepository.findIdsByPostIds(postIds).orElse(null);
		if (commentIds == null || commentIds.isEmpty()) {
			return;
		}

		final EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			likesCommentRepository.deleteByCommentIdIn(commentIds);
			commentRepository.deleteByPostIdIn(postIds);
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	private boolean hasPermissionToDeleteComment(final int id, final String userEmail) {
		final Comment comment = commentRepository.findCommentById(id);
		final User commenter = comment.getCommenter();
		final User organizer = comment.getPost().getEvent().getOrganizer();
		final String organizerEmail = organizer == null ? null : organizer.getEmail();
		final String commenterEmail = commenter == null ? null : commenter.getEmail();

		return userEmail.equals(commenterEmail) || userEmail.equals(organizerEmail);
	}
}
