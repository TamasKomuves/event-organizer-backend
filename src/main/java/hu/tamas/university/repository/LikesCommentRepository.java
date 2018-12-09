package hu.tamas.university.repository;

import hu.tamas.university.entity.LikesComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesCommentRepository extends JpaRepository<LikesComment, Long> {

	LikesComment findLikesCommentById(int id);


}
