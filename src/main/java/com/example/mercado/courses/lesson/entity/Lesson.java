package com.example.mercado.courses.lesson.entity;

import com.example.mercado.courses.lesson.common.base.BaseEntityLesson;
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
public class Lesson extends BaseEntityLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_id")
    @Setter
    private Long moduleId;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "description", length = 1000)
    @Setter
    private String description;

    @Column(name = "duration")
    @Setter
    private Integer duration;
}
