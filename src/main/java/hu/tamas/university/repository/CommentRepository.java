package hu.tamas.university.repository;

import hu.tamas.university.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	Comment findCommentById(int id);

	@Modifying
	@Transactional
	int deleteByPostIdIn(List<Integer> postIds);

	@Query("SELECT id FROM Comment WHERE post_id IN (:postIds)")
	Optional<List<Integer>> findIdsByPostIds(@Param("postIds") List<Integer> postIds);

	@Modifying
	@Transactional
	@Query("UPDATE Comment SET commenter_email = NULL where commenter_email = :userEmail")
	int updateByUserEmailSetCommenterEmailToNull(@Param("userEmail") String userEmail);
}
