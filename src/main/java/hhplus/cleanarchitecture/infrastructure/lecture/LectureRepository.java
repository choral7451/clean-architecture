package hhplus.cleanarchitecture.infrastructure.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.cleanarchitecture.domain.lecture.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
