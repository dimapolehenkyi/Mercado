package com.example.mercado.courses.lessonContent.entity;

import com.example.mercado.courses.lessonContent.base.BaseEntityLessonContent;
import com.example.mercado.courses.lessonContent.enums.LessonContentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Table(
        name = "lesson_content",
        indexes = {
                @Index(
                        name = "idx_lesson_id",
                        columnList = "lesson_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_name_lesson_id",
                        columnNames = {"name", "lesson_id"}
                ),
                @UniqueConstraint(
                        name = "uk_position_lesson_id",
                        columnNames = {"position", "lesson_id"}
                )
        }
)
public class LessonContent extends BaseEntityLessonContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "position")
    @Setter
    private Integer position;

    @Setter
    @Column(name = "url")
    private String url;

    @Setter
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(name = "type")
    private LessonContentType type;
}
