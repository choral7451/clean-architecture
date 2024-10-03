package hhplus.cleanarchitecture.interfaces.api.lecture.exception;

import hhplus.cleanarchitecture.interfaces.api.common.exception.CommonException;

public class LectureScheduleNotFound extends CommonException {
	private static final String MESSAGE = "LECTURE_SCHEDULE_NOT_FOUND";

	public LectureScheduleNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
