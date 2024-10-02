package hhplus.cleanarchitecture.interfaces.api.user.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hhplus.cleanarchitecture.application.user.UserService;
import hhplus.cleanarchitecture.interfaces.api.common.dto.response.CreateResponse;
import hhplus.cleanarchitecture.interfaces.api.user.dto.request.UserScheduleRegisterRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@PostMapping("/{userId}/register-schedule")
	public ResponseEntity<CreateResponse> registerSchedule(@PathVariable Long userId, @RequestBody UserScheduleRegisterRequest request) {
		return new ResponseEntity<>( new CreateResponse(userService.registerSchedule(request.toCommand(userId))), HttpStatus.OK);
	}
}
