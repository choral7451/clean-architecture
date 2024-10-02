package hhplus.cleanarchitecture.interfaces.api.user.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hhplus.cleanarchitecture.application.user.UserService;
import hhplus.cleanarchitecture.application.user.dto.query.UserSchedulesQuery;
import hhplus.cleanarchitecture.interfaces.api.common.dto.response.CreateResponse;
import hhplus.cleanarchitecture.interfaces.api.user.dto.request.UserScheduleRegisterRequest;
import hhplus.cleanarchitecture.interfaces.api.user.dto.response.UserScheduleResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@GetMapping("/{userId}/schedules")
	public ResponseEntity<List<UserScheduleResponse>> userSchedules(@PathVariable Long userId) {
		UserSchedulesQuery query = new UserSchedulesQuery(userId);
		return new ResponseEntity<>(userService.userSchedules(query).stream().map(UserScheduleResponse::new).toList(),  HttpStatus.OK);
	}

	@PostMapping("/{userId}/register-schedule")
	public ResponseEntity<CreateResponse> registerSchedule(@PathVariable Long userId, @RequestBody UserScheduleRegisterRequest request) {
		return new ResponseEntity<>( new CreateResponse(userService.registerSchedule(request.toCommand(userId))), HttpStatus.OK);
	}
}
