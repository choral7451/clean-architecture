package hhplus.cleanarchitecture.interfaces.api.user.exception;

import hhplus.cleanarchitecture.interfaces.api.common.exception.CommonException;

public class UserScheduleAlreadyExists extends CommonException {
	private static final String MESSAGE = "USER_SCHEDULE_ALREADY_EXISTS";

	public UserScheduleAlreadyExists() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 409;
	}
}
