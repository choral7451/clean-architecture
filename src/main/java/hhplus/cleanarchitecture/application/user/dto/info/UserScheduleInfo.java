package hhplus.cleanarchitecture.application.user.dto.info;

import java.time.LocalDateTime;

import hhplus.cleanarchitecture.domain.user.UserSchedule;
import lombok.Getter;

@Getter
public class UserScheduleInfo {
	private final Long userScheduleId;
	private final String lectureName;
	private final LocalDateTime startDate;
	private final LocalDateTime endDate;

	public UserScheduleInfo(UserSchedule userSchedule) {
		this.userScheduleId = userSchedule.getId();
		this.lectureName = userSchedule.getLectureSchedule().getLecture().getName();
		this.startDate = userSchedule.getLectureSchedule().getStartDate();
		this.endDate = userSchedule.getLectureSchedule().getEndDate();
	}
}
