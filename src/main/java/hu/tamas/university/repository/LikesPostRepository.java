package hu.tamas.university.repository;

import hu.tamas.university.entity.LikesPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface LikesPostRepository extends JpaRepository<LikesPost, Integer> {

	@Modifying
	@Transactional
	int deleteByPostIdIn(List<Integer> postIds);

	@Modifying
	@Transactional
	int deleteByUserEmail(String userEmail);
}
