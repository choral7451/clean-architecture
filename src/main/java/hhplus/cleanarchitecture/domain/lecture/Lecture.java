package hhplus.cleanarchitecture.domain.lecture;

import java.util.List;

import hhplus.cleanarchitecture.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "lectures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "current_number", nullable = false, columnDefinition = "int default 0")
	private Integer currentNumber = 0;

	@Column(name = "maximum_number", nullable = false, columnDefinition = "int default 30")
	private Integer maximumNumber = 30;
}
