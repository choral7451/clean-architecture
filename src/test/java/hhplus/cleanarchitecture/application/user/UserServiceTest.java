package hhplus.cleanarchitecture.application.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.cleanarchitecture.application.user.dto.command.UserScheduleRegisterCommand;
import hhplus.cleanarchitecture.domain.lecture.Lecture;
import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import hhplus.cleanarchitecture.domain.user.User;
import hhplus.cleanarchitecture.domain.user.UserSchedule;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserScheduleRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private LectureScheduleRepository lectureScheduleRepository;

	@Mock
	private UserScheduleRepository userScheduleRepository;

	@Test
	@DisplayName("유저가 강의를 신청합니다.")
	public void registerSchedule() {
		// given
		User user = User.builder().name("테스트").build();
		Lecture lecture = Lecture.builder().name("테스트강의").build();
		LectureSchedule lectureSchedule = LectureSchedule.builder().startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).lecture(lecture).build();

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(lectureScheduleRepository.findById(anyLong())).thenReturn(Optional.of(lectureSchedule));

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(1L)
			.lectureScheduleId(1L)
			.build();

		Long expectedUserScheduleId = userService.registerSchedule(command);

		assertEquals(lectureSchedule.getId(), expectedUserScheduleId);

		verify(userRepository).findById(anyLong());
		verify(lectureScheduleRepository).findById(anyLong());
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

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.registerSchedule(command);
		});

		assertEquals("USER_NOT_FOUND", exception.getMessage());

		verify(userRepository).findById(anyLong());
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

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(userId)
			.lectureScheduleId(lectureScheduleId)
			.build();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.registerSchedule(command);
		});

		assertEquals("LECTURE_SCHEDULE_NOT_FOUND", exception.getMessage());

		verify(userRepository).findById(anyLong());
		verify(lectureScheduleRepository).findById(anyLong());
		verify(userScheduleRepository, never()).findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong());
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

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(lectureScheduleRepository.findById(anyLong())).thenReturn(Optional.of(lectureSchedule));
		when(userScheduleRepository.findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong())).thenReturn(Optional.of(userSchedule));

		UserScheduleRegisterCommand command = UserScheduleRegisterCommand.builder()
			.userId(userId)
			.lectureScheduleId(lectureScheduleId)
			.build();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.registerSchedule(command);
		});

		assertEquals("USER_SCHEDULE_ALREADY_EXISTS", exception.getMessage());

		verify(userRepository).findById(anyLong());
		verify(lectureScheduleRepository).findById(anyLong());
		verify(userScheduleRepository).findUserScheduleByUserIdAndLectureSchedule_Id(anyLong(), anyLong());
	}
}