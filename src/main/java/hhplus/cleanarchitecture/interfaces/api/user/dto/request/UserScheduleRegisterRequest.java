package hhplus.cleanarchitecture.interfaces.api.user.dto.request;

import hhplus.cleanarchitecture.application.user.dto.command.UserScheduleRegisterCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserScheduleRegisterRequest {
	private Long lectureScheduleId;

	public UserScheduleRegisterCommand toCommand(Long userId) {
		return UserScheduleRegisterCommand.builder()
			.userId(userId)
			.lectureScheduleId(lectureScheduleId)
			.build();
	}
}
