	package hhplus.cleanarchitecture.interfaces.api.lecture;

	import static org.springframework.http.MediaType.*;
	import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
	import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

	import java.time.LocalDateTime;
	import java.util.List;

	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.DisplayName;
	import org.junit.jupiter.api.Test;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.test.web.servlet.MockMvc;

	import hhplus.cleanarchitecture.domain.lecture.Lecture;
	import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
	import hhplus.cleanarchitecture.infrastructure.lecture.LectureRepository;
	import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;

	@SpringBootTest
	@AutoConfigureMockMvc
	class LectureControllerTest {
		@Autowired
		private MockMvc mockMvc;

		@Autowired
		private LectureRepository lectureRepository;

		@Autowired
		private LectureScheduleRepository lectureScheduleRepository;

		@BeforeEach()
		void clean() {
			lectureScheduleRepository.deleteAll();
			lectureRepository.deleteAll();
		}

		@Test
		@DisplayName("강의 일정 목록 조회")
		public void lectureSchedules() throws Exception {
			// given

			Lecture givenLecture = Lecture.builder().name("테스트 강의").build();
			lectureRepository.save(givenLecture);

			LocalDateTime givenStartDate = LocalDateTime.of(2023, 10, 1, 12, 12,12);
			LocalDateTime givenEndDate = LocalDateTime.of(2023, 10, 1, 12, 12,12);

			List<LectureSchedule> givenLectureSchedules = List.of(
				LectureSchedule.builder()
					.lecture(givenLecture)
					.startDate(givenStartDate)
					.endDate(givenEndDate.plusDays(1))
					.build(),
				LectureSchedule.builder()
					.lecture(givenLecture)
					.startDate(givenStartDate.plusDays(2))
					.endDate(givenEndDate.plusDays(3))
					.build(),
				LectureSchedule.builder()
					.lecture(givenLecture)
					.startDate(givenStartDate.plusDays(4))
					.endDate(givenEndDate.plusDays(5))
					.build()
			);
			lectureScheduleRepository.saveAll(givenLectureSchedules);

			// when
			mockMvc.perform(get("/lectures")
					.contentType(APPLICATION_JSON))

				// then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(givenLectureSchedules.get(0).getId()))
				.andExpect(jsonPath("$[0].startDate").value(givenLectureSchedules.get(0).getStartDate().toString()))
				.andExpect(jsonPath("$[0].endDate").value(givenLectureSchedules.get(0).getEndDate().toString()))
				.andExpect(jsonPath("$[1].id").value(givenLectureSchedules.get(1).getId().toString()))
				.andExpect(jsonPath("$[1].startDate").value(givenLectureSchedules.get(1).getStartDate().toString()))
				.andExpect(jsonPath("$[1].endDate").value(givenLectureSchedules.get(1).getEndDate().toString()))
				.andExpect(jsonPath("$[2].id").value(givenLectureSchedules.get(2).getId().toString()))
				.andExpect(jsonPath("$[2].startDate").value(givenLectureSchedules.get(2).getStartDate().toString()))
				.andExpect(jsonPath("$[2].endDate").value(givenLectureSchedules.get(2).getEndDate().toString()));
		}
	}