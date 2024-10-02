package hhplus.cleanarchitecture.interfaces.api.common.dto.response;

import lombok.Getter;

@Getter
public class CreateResponse {
	private final Long id;

	public CreateResponse(Long id) {
		this.id = id;
	}
}
