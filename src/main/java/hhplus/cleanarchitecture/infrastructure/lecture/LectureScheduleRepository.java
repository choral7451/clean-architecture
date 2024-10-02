package hhplus.cleanarchitecture.infrastructure.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;

public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {}
