package hhplus.cleanarchitecture.interfaces.api.lecture.dto.response;

import java.time.LocalDateTime;

import hhplus.cleanarchitecture.application.lecture.dto.info.LectureScheduleInfo;
import lombok.Getter;

@Getter
public class LectureScheduleResponse {
	private Long id;
	private String name;
	private Integer maximumNumber;
	private Integer currentNumber;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public LectureScheduleResponse(LectureScheduleInfo lectureScheduleInfo) {
		this.id = lectureScheduleInfo.getId();
		this.name = lectureScheduleInfo.getName();
		this.maximumNumber = lectureScheduleInfo.getMaximumNumber();
		this.currentNumber = lectureScheduleInfo.getCurrentNumber();
		this.startDate = lectureScheduleInfo.getStartDate();
		this.endDate = lectureScheduleInfo.getEndDate();
	}
}
