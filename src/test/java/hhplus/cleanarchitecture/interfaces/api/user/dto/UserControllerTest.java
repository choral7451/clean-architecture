package hhplus.cleanarchitecture.interfaces.api.user.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import hhplus.cleanarchitecture.domain.lecture.Lecture;
import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import hhplus.cleanarchitecture.domain.user.User;
import hhplus.cleanarchitecture.domain.user.UserSchedule;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureRepository;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserRepository;
import hhplus.cleanarchitecture.infrastructure.user.UserScheduleRepository;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserScheduleRepository userScheduleRepository;

	@Autowired
	private LectureRepository lectureRepository;

	@Autowired
	private LectureScheduleRepository lectureScheduleRepository;

	@BeforeEach()
	void clean() {
		userScheduleRepository.deleteAll();
		lectureScheduleRepository.deleteAll();
		lectureRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("유저 강의 등록")
	public void registerSchedule() throws Exception {
		// given
		User givenUser = User.builder().name("테서터").build();
		userRepository.save(givenUser);

		Lecture givenLecture = Lecture.builder().name("테스트 강의").build();
		lectureRepository.save(givenLecture);

		LectureSchedule givenLectureSchedule = LectureSchedule.builder()
			.lecture(givenLecture)
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1))
			.build();
		lectureScheduleRepository.save(givenLectureSchedule);

		String requestBody = String.format("{\"lectureScheduleId\": %d}", givenLectureSchedule.getId());

		// when
		mockMvc.perform(post("/users/{userId}/register-schedule", givenUser.getId())
				.contentType(APPLICATION_JSON)
				.content(requestBody)
			)

			// then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNumber());
	}

	@Test
	@DisplayName("유저 등록 강의 일정 목록 조회")
	public void userSchedules() throws Exception {
		// given
		User givenUser = User.builder().name("테서터").build();
		userRepository.save(givenUser);

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
				.build()
		);
		lectureScheduleRepository.saveAll(givenLectureSchedules);

		List<UserSchedule> userSchedules = List.of(
			UserSchedule.builder().user(givenUser).lectureSchedule(givenLectureSchedules.get(0)).build(),
			UserSchedule.builder().user(givenUser).lectureSchedule(givenLectureSchedules.get(1)).build()
		);
		userScheduleRepository.saveAll(userSchedules);

		// when
		mockMvc.perform(get("/users/{userId}/schedules", givenUser.getId())
				.contentType(APPLICATION_JSON))

			// then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].userScheduleId").isNumber())
			.andExpect(jsonPath("$[0].lectureName").value(givenLecture.getName()))
			.andExpect(jsonPath("$[0].startDate").value(givenLectureSchedules.get(0).getStartDate().toString()))
			.andExpect(jsonPath("$[0].endDate").value(givenLectureSchedules.get(0).getEndDate().toString()))
			.andExpect(jsonPath("$[1].userScheduleId").isNumber())
			.andExpect(jsonPath("$[1].lectureName").value(givenLecture.getName()))
			.andExpect(jsonPath("$[1].startDate").value(givenLectureSchedules.get(1).getStartDate().toString()))
			.andExpect(jsonPath("$[1].endDate").value(givenLectureSchedules.get(1).getEndDate().toString()));
	}

	@Test
	@DisplayName("강의 등록은 최대 30명까지 가능합니다")
	public void registerScheduleMaximumSize() throws Exception {
		// given
		List<User> givenUsers = new ArrayList<>();
		for(int i = 0; i < 40; i++) {
			User user = User.builder().name("테스트" + i).build();
			givenUsers.add(user);
		}
		userRepository.saveAll(givenUsers);

		Lecture givenLecture = Lecture.builder().name("테스트 강의").build();
		lectureRepository.save(givenLecture);

		LectureSchedule givenLectureSchedule = LectureSchedule.builder()
			.lecture(givenLecture)
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1))
			.build();
		lectureScheduleRepository.save(givenLectureSchedule);

		int threadCount = 40;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		List<Callable<MvcResult>> callables = new ArrayList<>();

		for (int i = 0; i < threadCount; i++) {
			int index = i;
			String requestBody = String.format("{\"lectureScheduleId\": %d}", givenLectureSchedule.getId());
			callables.add(() -> {
				return mockMvc.perform(post("/users/{userId}/register-schedule", givenUsers.get(index).getId())
					.contentType(APPLICATION_JSON)
					.content(requestBody)
				).andReturn();
			});
		}

		List<Future<MvcResult>> futures = executorService.invokeAll(callables);

		int successCount = 0;
		int errorCount = 0;

		for (Future<MvcResult> future : futures) {
			MvcResult result = future.get();
			int status = result.getResponse().getStatus();

			if (status == 200) {
				successCount++;
			} else {
				errorCount++;
			}
		}

		assertEquals(30, successCount, "성공한 요청의 수는 30이어야 합니다.");
		assertEquals(10, errorCount, "실패한 요청의 수는 10이어야 합니다.");

		executorService.shutdown();
	}
}