package com.example.mercado.enrollment.entity;

import com.example.mercado.common.entity.BaseEntity;
import com.example.mercado.enrollment.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "enrollment_table")
public class Enrollment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    @Setter
    private Long userId;

    @Column(name = "course_id")
    @Setter
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

}
