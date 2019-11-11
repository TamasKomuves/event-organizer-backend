package hu.tamas.university.repository;

import hu.tamas.university.entity.LikesComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface LikesCommentRepository extends JpaRepository<LikesComment, Integer> {

	LikesComment findLikesCommentById(int id);

	Optional<List<LikesComment>> findByCommentId(int commentId);

	Optional<LikesComment> findByCommentIdAndUserEmail(int commentId, String userEmail);

	@Modifying
	@Transactional
	int deleteByCommentIdIn(List<Integer> commentIds);

	@Modifying
	@Transactional
	int deleteByUserEmail(String userEmail);
}
