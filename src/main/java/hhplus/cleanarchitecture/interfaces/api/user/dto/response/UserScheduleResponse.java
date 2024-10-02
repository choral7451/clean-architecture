package hhplus.cleanarchitecture.interfaces.api.user.dto.response;

import java.time.LocalDateTime;

import hhplus.cleanarchitecture.application.user.dto.info.UserScheduleInfo;
import lombok.Getter;

@Getter
public class UserScheduleResponse {
	private final Long userScheduleId;
	private final String lectureName;
	private final LocalDateTime startDate;
	private final LocalDateTime endDate;

	public UserScheduleResponse(UserScheduleInfo userScheduleInfo) {
		this.userScheduleId = userScheduleInfo.getUserScheduleId();
		this.lectureName = userScheduleInfo.getLectureName();
		this.startDate = userScheduleInfo.getStartDate();
		this.endDate = userScheduleInfo.getEndDate();
	}
}
