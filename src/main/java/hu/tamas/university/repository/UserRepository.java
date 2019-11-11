package hu.tamas.university.repository;

import hu.tamas.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	@Modifying
	@Transactional
	int deleteByEmail(String email);
}
