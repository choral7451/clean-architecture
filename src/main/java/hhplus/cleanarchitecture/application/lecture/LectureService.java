package hhplus.cleanarchitecture.application.lecture;

import java.util.List;

import org.springframework.stereotype.Service;

import hhplus.cleanarchitecture.application.lecture.dto.info.LectureScheduleInfo;
import hhplus.cleanarchitecture.infrastructure.lecture.LectureScheduleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
	private final LectureScheduleRepository lectureScheduleRepository;

	public List<LectureScheduleInfo> getScheduleList() {
		return this.lectureScheduleRepository.findAll().stream().map(LectureScheduleInfo::new).toList();
	}
}
