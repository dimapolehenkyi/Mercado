package com.example.mercado.courses.course.courseLearningPoint.entity;

import com.example.mercado.courses.course.common.base.BaseEntity;
import com.example.mercado.courses.course.courseLearningPoint.exception.CourseLearningPointAlreadyHaveSameTextException;
import com.example.mercado.courses.course.courseLearningPoint.exception.CourseLearningPointPositionBelowZeroException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "course_learning_points",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_position_course_id",
                        columnNames = {"course_id", "position"}
                )
        }
)
public class CourseLearningPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Column(name = "position", nullable = false)
    private Integer position;


    public void setText(String text) {
        if (Objects.equals(this.text, text)) {
            throw new CourseLearningPointAlreadyHaveSameTextException(id);
        }
        this.text = text;
    }

    public void setPosition(Integer position) {
        if (position < 0) {
            throw new CourseLearningPointPositionBelowZeroException(id);
        }
        this.position = position;
    }

}
