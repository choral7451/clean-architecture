package hhplus.cleanarchitecture.application.user.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserScheduleRegisterCommand {
	private final Long userId;
	private final Long lectureScheduleId;
}
