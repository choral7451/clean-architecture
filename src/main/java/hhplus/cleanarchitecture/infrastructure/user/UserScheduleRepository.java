package hhplus.cleanarchitecture.infrastructure.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.cleanarchitecture.domain.user.UserSchedule;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
	List<UserSchedule> findByUserId(Long userId);
	Optional<UserSchedule> findUserScheduleByUserIdAndLectureSchedule_Id(Long userId, Long lectureScheduleId);
}
