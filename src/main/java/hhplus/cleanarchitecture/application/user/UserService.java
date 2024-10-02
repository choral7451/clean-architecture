package hhplus.cleanarchitecture.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hhplus.cleanarchitecture.application.user.dto.command.UserScheduleRegisterCommand;
import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import hhplus.cleanarchitecture.domain.user.User;
import hhplus.cleanarchitecture.domain.user.UserSchedule;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserScheduleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final LectureScheduleRepository lectureScheduleRepository;
	private final UserScheduleRepository userScheduleRepository;

	public Long registerSchedule(UserScheduleRegisterCommand command) {
		User user = userRepository.findById(command.getUserId()).orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
		LectureSchedule lectureSchedule = lectureScheduleRepository.findById(command.getLectureScheduleId()).orElseThrow(() -> new IllegalArgumentException("LECTURE_SCHEDULE_NOT_FOUND"));
		Optional<UserSchedule> userSchedule = userScheduleRepository.findUserScheduleByUserIdAndLectureSchedule_Id(
			command.getUserId(), command.getLectureScheduleId());
		if (userSchedule.isPresent()) {
			throw new IllegalArgumentException("USER_SCHEDULE_ALREADY_EXISTS");
		}

		UserSchedule newUserSchedule = UserSchedule.builder()
			.user(user)
			.lectureSchedule(lectureSchedule)
			.build();

		userScheduleRepository.save(newUserSchedule);

		return newUserSchedule.getId();
 	}
}
