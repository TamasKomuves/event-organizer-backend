package hu.tamas.university.repository;

import hu.tamas.university.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	Comment findCommentById(int id);
}
