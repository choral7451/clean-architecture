package hhplus.cleanarchitecture.infrastructure.lecture;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import jakarta.persistence.LockModeType;

public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT l FROM LectureSchedule l WHERE l.id = :id")
	Optional<LectureSchedule> findByIdWithLock(@Param("id") Long id);
}
