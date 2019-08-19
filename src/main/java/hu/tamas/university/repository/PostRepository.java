package hu.tamas.university.repository;

import hu.tamas.university.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Post findPostById(int id);

	List<Post> findAllByEventId(int eventId);
}
