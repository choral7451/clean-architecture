package hhplus.cleanarchitecture.application.lecture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.cleanarchitecture.application.lecture.dto.info.LectureScheduleInfo;
import hhplus.cleanarchitecture.domain.lecture.Lecture;
import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {
	@InjectMocks
	private LectureService lectureService;

	@Mock
	private LectureScheduleRepository lectureScheduleRepository;

	@Test
	@DisplayName("강의 일정 목록 조회")
	public void lectureSchedules() {
		// given
		Long userId = 1L;

		Lecture lecture = Lecture.builder().name("테스트강의").build();
		List<LectureSchedule> givenLectureSchedules = List.of(
			LectureSchedule.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(1))
				.lecture(lecture)
				.build(),
			LectureSchedule.builder()
				.startDate(LocalDateTime.now().plusDays(2))
				.endDate(LocalDateTime.now().plusDays(3))
				.lecture(lecture)
				.build(),
			LectureSchedule.builder()
				.startDate(LocalDateTime.now().plusDays(4))
				.endDate(LocalDateTime.now().plusDays(5))
				.lecture(lecture)
				.build()
		);

		when(lectureScheduleRepository.findAll()).thenReturn(givenLectureSchedules);

		// when
		List<LectureScheduleInfo> lectureSchedules = lectureService.getScheduleList();

		// then
		assertEquals(givenLectureSchedules.get(0).getId(), lectureSchedules.get(0).getId());
		assertEquals(givenLectureSchedules.get(0).getStartDate(), lectureSchedules.get(0).getStartDate());
		assertEquals(givenLectureSchedules.get(0).getEndDate(), lectureSchedules.get(0).getEndDate());
		assertEquals(givenLectureSchedules.get(1).getId(), lectureSchedules.get(1).getId());
		assertEquals(givenLectureSchedules.get(1).getStartDate(), lectureSchedules.get(1).getStartDate());
		assertEquals(givenLectureSchedules.get(1).getEndDate(), lectureSchedules.get(1).getEndDate());
		assertEquals(givenLectureSchedules.get(2).getId(), lectureSchedules.get(2).getId());
		assertEquals(givenLectureSchedules.get(2).getStartDate(), lectureSchedules.get(2).getStartDate());
		assertEquals(givenLectureSchedules.get(2).getEndDate(), lectureSchedules.get(2).getEndDate());

		verify(lectureScheduleRepository).findAll();
	}
}