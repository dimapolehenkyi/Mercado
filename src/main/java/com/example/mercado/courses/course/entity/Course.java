package com.example.mercado.courses.course.entity;

import com.example.mercado.courses.course.common.base.BaseEntityCourse;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "courses_table")
public class Course extends BaseEntityCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    @Setter
    private Long userId;

    @Column(name = "teacher_id",  nullable = false)
    @Setter
    private Long teacherId;

    @Column(name = "name", nullable = false, unique = true)
    @Setter
    private String name;

    @Column(name = "description", length = 2000)
    @Setter
    private String description;

    @Builder.Default
    @Setter
    @Column(name = "student_count", nullable = false)
    private Long studentCount = 0L;

    @Column(name = "price",  nullable = false)
    @Setter
    private BigDecimal price;

    @Setter
    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @Setter
    @Builder.Default
    @Column(name = "deleted",  nullable = false)
    private Boolean deleted = false;
}
