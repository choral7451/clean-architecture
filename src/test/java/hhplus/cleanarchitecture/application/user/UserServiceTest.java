package hhplus.cleanarchitecture.application.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import hhplus.cleanarchitecture.interfaces.api.lecture.exception.LectureScheduleNotFound;
import hhplus.cleanarchitecture.interfaces.api.user.exception.UserNotFound;
import hhplus.cleanarchitecture.interfaces.api.user.exception.UserScheduleAlreadyExists;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private LectureRepository lectureRepository;

	@Mock
	private LectureScheduleRepository lectureScheduleRepository;

	@Mock
	private UserScheduleRepository userScheduleRepository;

	@Test
	@DisplayName("유저의 강의 일정 목록을 조회합니다.")
	public void userSchedules() {
		// given
		Long userId = 1L;

		User user = User.builder().name("테스트").build();
		Lecture lecture = Lecture.builder().name("테스트강의").build();
		LectureSchedule lectureSchedule1 = LectureSchedule.builder().startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).lecture(lecture).build();
		LectureSchedule lectureSchedule2 = LectureSchedule.builder().startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).lecture(lecture).build();
		List<UserSchedule> userSchedules = List.of(
			UserSchedule.builder().user(user).lectureSchedule(lectureSchedule1).build(),
			UserSchedule.builder().user(user).lectureSchedule(lectureSchedule2).build()
		);

		when(userScheduleRepository.findByUserId(anyLong())).thenReturn(userSchedules);

		UserSchedulesQuery query = new UserSchedulesQuery(userId);

		// when
		List<UserScheduleInfo> expectedUserSchedules = userService.userSchedules(query);

		// then
		assertEquals(lectureSchedule1.getId(), expectedUserSchedules.get(0).getUserScheduleId());
		assertEquals(lectureSchedule1.getLecture().getName(), expectedUserSchedules.get(0).getLectureName());
		assertEquals(lectureSchedule1.getStartDate(), expectedUserSchedules.get(0).getStartDate());
		assertEquals(lectureSchedule1.getEndDate(), expectedUserSchedules.get(0).getEndDate());
		assertEquals(lectureSchedule2.getId(), expectedUserSchedules.get(1).getUserScheduleId());
		assertEquals(lectureSchedule2.getLecture().getName(), expectedUserSchedules.get(1).getLectureName());
		assertEquals(lectureSchedule2.getStartDate(), expectedUserSchedules.get(1).getStartDate());
		assertEquals(lectureSchedule2.getEndDate(), expectedUserSchedules.get(1).getEndDate());

		verify(userScheduleRepository).findByUserId(anyLong());
	}

	@Test
	@DisplayName("유저가 강의를 신청합니다.")
	public void registerSchedule() {
		// given
		User user = User.builder().name("테스트").build();
		Lecture lecture = Lecture.builder().name("테스트강의").build();
		LectureSchedule lectureSchedule = LectureSchedule.builder().startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).lecture(lecture).build();

		when(userRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(user));
		when(lectureScheduleRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(lectureSchedule));

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(1L)
			.lectureScheduleId(1L)
			.build();

		// when
		Long expectedUserScheduleId = userService.registerSchedule(command);

		// then
		assertEquals(lectureSchedule.getId(), expectedUserScheduleId);

		verify(userRepository).findByIdWithLock(anyLong());
		verify(lectureScheduleRepository).findByIdWithLock(anyLong());
		verify(userScheduleRepository).findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong());
	}

	@Test
	@DisplayName("유효하지 않은 유저가 강의를 신청합니다.")
	public void invalidUserRegisterSchedule() throws Exception {
		// given
		Long userId = 1L;
		Long lectureScheduleId = 1L;

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(userId)
			.lectureScheduleId(lectureScheduleId)
			.build();

		UserNotFound exception = assertThrows(UserNotFound.class, () -> {
			userService.registerSchedule(command);
		});

		assertEquals("USER_NOT_FOUND", exception.getMessage());

		verify(userRepository).findByIdWithLock(anyLong());
		verify(lectureScheduleRepository, never()).findById(anyLong());
		verify(userScheduleRepository, never()).findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong());
	}

	@Test
	@DisplayName("유저가 유효하지 않은 강의를 신청합니다.")
	public void  userRegisterInvalidSchedule() throws Exception {
		// given
		Long userId = 1L;
		Long lectureScheduleId = 1L;

		User user = User.builder().name("테스트").build();

		when(userRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(user));

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(userId)
			.lectureScheduleId(lectureScheduleId)
			.build();

		LectureScheduleNotFound exception = assertThrows(LectureScheduleNotFound.class, () -> {
			userService.registerSchedule(command);
		});

		assertEquals("LECTURE_SCHEDULE_NOT_FOUND", exception.getMessage());

		verify(userRepository).findByIdWithLock(anyLong());
		verify(userScheduleRepository).findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong());
		verify(lectureScheduleRepository).findByIdWithLock(anyLong());
	}

	@Test
	@DisplayName("유저가 강의를 중복 신청합니다.")
	public void  userRegisterDuplicateSchedule() throws Exception {
		// given
		Long userId = 1L;
		Long lectureScheduleId = 1L;

		User user = User.builder().name("테스트").build();
		Lecture lecture = Lecture.builder().name("테스트강의").build();
		LectureSchedule lectureSchedule = LectureSchedule.builder().startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).lecture(lecture).build();
		UserSchedule userSchedule = UserSchedule.builder().user(user).lectureSchedule(lectureSchedule).build();

		when(userRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(user));
		when(userScheduleRepository.findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong())).thenReturn(Optional.of(userSchedule));

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(userId)
			.lectureScheduleId(lectureScheduleId)
			.build();

		UserScheduleAlreadyExists exception = assertThrows(UserScheduleAlreadyExists.class, () -> {
			userService.registerSchedule(command);
		});

		assertEquals("USER_SCHEDULE_ALREADY_EXISTS", exception.getMessage());

		verify(userRepository).findByIdWithLock(anyLong());
		verify(lectureScheduleRepository, never()).findById(anyLong());
		verify(userScheduleRepository).findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong());
	}
}