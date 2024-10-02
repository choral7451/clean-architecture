package hhplus.cleanarchitecture.interfaces.api.lecture;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hhplus.cleanarchitecture.application.lecture.LectureService;
import hhplus.cleanarchitecture.interfaces.api.lecture.dto.response.LectureScheduleResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {
	private final LectureService lectureService;

	@GetMapping
	public ResponseEntity<List<LectureScheduleResponse>> companies() {
		return  new ResponseEntity<>(lectureService.getScheduleList().stream().map(LectureScheduleResponse::new).toList(), HttpStatus.OK);
	}
}
