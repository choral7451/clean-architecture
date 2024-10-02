package hhplus.cleanarchitecture.application.user.dto.query;

import lombok.Getter;

@Getter
public class UserSchedulesQuery {
	private final Long userId;

	public UserSchedulesQuery(Long userId) {
		this.userId = userId;
	}
}
