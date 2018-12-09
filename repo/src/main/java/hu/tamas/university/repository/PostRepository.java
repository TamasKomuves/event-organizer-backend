package hu.tamas.university.repository;

import hu.tamas.university.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

	Post findPostById(int id);
}
