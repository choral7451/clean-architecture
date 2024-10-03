package hhplus.cleanarchitecture.application.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.cleanarchitecture.application.user.dto.command.UserScheduleRegisterCommand;
import hhplus.cleanarchitecture.application.user.dto.info.UserScheduleInfo;
import hhplus.cleanarchitecture.application.user.dto.query.UserSchedulesQuery;
import hhplus.cleanarchitecture.domain.lecture.Lecture;
import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import hhplus.cleanarchitecture.domain.user.User;
import hhplus.cleanarchitecture.domain.user.UserSchedule;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureRepository;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserScheduleRepository;
import hhplus.cleanarchitecture.interfaces.api.lecture.exception.LectureMaximumNumberExceeded;
import hhplus.cleanarchitecture.interfaces.api.lecture.exception.LectureScheduleNotFound;
import hhplus.cleanarchitecture.interfaces.api.user.exception.UserNotFound;
import hhplus.cleanarchitecture.interfaces.api.user.exception.UserScheduleAlreadyExists;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final LectureScheduleRepository lectureScheduleRepository;
	private final UserScheduleRepository userScheduleRepository;
	private final LectureRepository lectureRepository;

	@Transactional(readOnly = true)
	public List<UserScheduleInfo> userSchedules(UserSchedulesQuery query) {
		return userScheduleRepository.findByUserId(query.getUserId()).stream().map(UserScheduleInfo::new).toList();
	}


	@Transactional
	public Long registerSchedule(UserScheduleRegisterCommand command) {
		User user = userRepository.findByIdWithLock(command.getUserId())
			.orElseThrow(UserNotFound::new);

		Optional<UserSchedule> userSchedule = userScheduleRepository.findUserScheduleByUserIdAndLectureSchedule_Id(
			command.getUserId(), command.getLectureScheduleId());
		if (userSchedule.isPresent()) {
			throw new UserScheduleAlreadyExists();
		}

		LectureSchedule lectureSchedule = lectureScheduleRepository.findByIdWithLock(command.getLectureScheduleId())
			.orElseThrow(LectureScheduleNotFound::new);

		Lecture lecture = lectureSchedule.getLecture();
		Integer lectureCurrentNumber = lecture.getCurrentNumber();
		if (lectureCurrentNumber >= lecture.getMaximumNumber()) {
			throw new LectureMaximumNumberExceeded();
		}
		lecture.editCurrentNumber(lectureCurrentNumber + 1);
		lectureRepository.save(lecture);

		UserSchedule newUserSchedule = UserSchedule.builder()
			.user(user)
			.lectureSchedule(lectureSchedule)
			.build();

		userScheduleRepository.save(newUserSchedule);

		return newUserSchedule.getId();
	}
}
