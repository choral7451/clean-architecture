package hhplus.cleanarchitecture.interfaces.api.lecture.exception;

import hhplus.cleanarchitecture.interfaces.api.common.exception.CommonException;

public class LectureMaximumNumberExceeded extends CommonException {
	private static final String MESSAGE = "LECTURE_MAXIMUM_NUMBER_EXCEEDED";

	public LectureMaximumNumberExceeded() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 409;
	}
}
