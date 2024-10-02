package hhplus.cleanarchitecture.domain.lecture;

import java.time.LocalDateTime;

import hhplus.cleanarchitecture.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "lecture_schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureSchedule extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "start_date", updatable = false, nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", updatable = false, nullable = false)
	private LocalDateTime endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Lecture lecture;

	@Builder
	public LectureSchedule(LocalDateTime startDate, LocalDateTime endDate, Lecture lecture) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.lecture = lecture;
	}
}
