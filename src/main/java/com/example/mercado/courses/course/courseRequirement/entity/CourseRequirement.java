package com.example.mercado.courses.course.courseRequirement.entity;

import com.example.mercado.courses.course.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

}
