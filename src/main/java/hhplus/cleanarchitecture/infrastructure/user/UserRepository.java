package hhplus.cleanarchitecture.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import hhplus.cleanarchitecture.domain.user.User;
import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM User u WHERE u.id = :id")
	Optional<User> findByIdWithLock(Long id);
}
