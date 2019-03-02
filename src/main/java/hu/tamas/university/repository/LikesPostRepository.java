package hu.tamas.university.repository;

import hu.tamas.university.entity.LikesPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesPostRepository extends JpaRepository<LikesPost, Integer> {

	LikesPost findLikesPostById(int id);
}
