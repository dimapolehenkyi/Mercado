package com.example.mercado.courses.course.courseLearningPoint.entity;

import com.example.mercado.common.entity.BaseEntity;
import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
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
                ),
                @UniqueConstraint(
                        name = "uk_text_course_id",
                        columnNames = {"course_id", "text"}
                )
        }
)
public class CourseLearningPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Column(name = "position", nullable = false)
    private Integer position;


    public void setText(String text) {
        if (Objects.equals(this.text, text)) {
            throw new AppException(
                    ErrorCode.LEARNING_POINT_SAME_TEXT
            );
        }
        this.text = text;
    }

    public void setPosition(Integer position) {
        if (position < 0) {
            throw new AppException(
                    ErrorCode.LEARNING_POINT_POSITION_INVALID
            );
        }
        this.position = position;
    }

}
