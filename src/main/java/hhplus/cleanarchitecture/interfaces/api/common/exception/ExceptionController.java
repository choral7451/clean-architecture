package hhplus.cleanarchitecture.interfaces.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import hhplus.cleanarchitecture.interfaces.api.common.dto.response.ErrorResponse;

@ControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
		ErrorResponse response = ErrorResponse.builder()
			.code("400")
			.message("BAD_REQUEST")
			.build();

		for (FieldError fieldError : e.getFieldErrors()) {
			response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return response;
	}

	@ResponseBody
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<ErrorResponse> CommonException(CommonException e) {
		int statusCode = e.getStatusCode();

		ErrorResponse body = ErrorResponse.builder()
			.code(String.valueOf(statusCode))
			.message(e.getMessage())
			.validation(e.getValidation())
			.build();


		return ResponseEntity.status(statusCode)
			.body(body);
	}
}
