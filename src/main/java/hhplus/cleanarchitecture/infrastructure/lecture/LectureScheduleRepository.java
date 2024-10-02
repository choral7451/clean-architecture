package hhplus.cleanarchitecture.infrastructure.lecture;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;

public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {
	Optional<LectureSchedule> findByUserIdAndLectureId(Long userId, Long lectureId);
}
