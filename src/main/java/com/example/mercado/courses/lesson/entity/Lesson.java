package com.example.mercado.courses.lesson.entity;

import com.example.mercado.common.entity.BaseEntity;
import com.example.mercado.courses.lesson.enums.LessonStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lessons")
public class Lesson extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_id")
    @Setter
    private Long moduleId;

    @Column(
            name = "name",
            length = 150
    )
    @Setter
    private String name;

    @Column(
            name = "description",
            length = 1000
    )
    @Setter
    private String description;

    @Column(name = "position")
    @Setter
    private Integer position;

    @Column(name = "duration")
    @Setter
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Setter
    private LessonStatus status;
}
