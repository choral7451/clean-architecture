package hhplus.cleanarchitecture.interfaces.api.common.dto.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private final String code;
	private final String message;

	@Builder.Default
	private final Map<String, String> validation = new HashMap<>();

	public void addValidation(String fieldName, String errorMessage) {
		this.validation.put(fieldName, errorMessage);
	}
}
