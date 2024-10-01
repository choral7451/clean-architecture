package hhplus.cleanarchitecture.domain.user;

import hhplus.cleanarchitecture.domain.BaseTimeEntity;
import hhplus.cleanarchitecture.domain.lecture.Lecture;
import hhplus.cleanarchitecture.domain.lecture.LectureSchedule;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSchedule extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_schedule_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private LectureSchedule lectureSchedule;
}
