package hhplus.cleanarchitecture.application.lecture.dto.info;

import java.time.LocalDateTime;

import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import lombok.Getter;

@Getter
public class LectureScheduleInfo {
	private Long id;
	private String name;
	private Integer maximumNumber;
	private Integer currentNumber;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public LectureScheduleInfo(LectureSchedule lectureSchedule) {
		this.id = lectureSchedule.getId();
		this.name = lectureSchedule.getLecture().getName();
		this.maximumNumber = lectureSchedule.getLecture().getMaximumNumber();
		this.currentNumber = lectureSchedule.getLecture().getCurrentNumber();
		this.startDate = lectureSchedule.getStartDate();
		this.endDate = lectureSchedule.getEndDate();
	}
}
