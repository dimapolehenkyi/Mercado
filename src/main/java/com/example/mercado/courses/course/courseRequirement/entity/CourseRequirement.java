package com.example.mercado.courses.course.courseRequirement.entity;

import com.example.mercado.courses.course.common.base.BaseEntity;
import com.example.mercado.courses.course.courseRequirement.exception.PositionBelowZeroException;
import com.example.mercado.courses.course.courseRequirement.exception.TextAreTheSameException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "course_requirements",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_position_course_id",
                        columnNames = {"course_id", "position"}
                )
        }
)
public class CourseRequirement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Column(name = "position", nullable = false)
    private Integer position;



    public void setPosition(Integer position) {

        if (position < 0) {
            throw new PositionBelowZeroException();
        }

        this.position = position;

    }

    public void setText(String text) {

        text = text.trim();

        if (Objects.equals(this.text, text)) {
            throw new TextAreTheSameException();
        }

        this.text = text;

    }
}
