package hhplus.cleanarchitecture.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.cleanarchitecture.domain.user.UserSchedule;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
	Optional<UserSchedule> findUserScheduleByUserIdAndLectureSchedule_Id(Long userId, Long lectureScheduleId);
}
