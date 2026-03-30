package com.example.mercado.courses.lesson.mapper;

import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.LessonShortResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;
import com.example.mercado.courses.lesson.entity.Lesson;
import com.example.mercado.courses.lesson.enums.LessonStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LessonMapper Test")
public class LessonMapperTest {

    private LessonMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LessonMapper();
    }

    @Test
    @DisplayName("Func toEntity should map all fields")
    void toEntity_shouldMapAllFields() {
        Long moduleId = 1L;
        CreateLessonRequest request = new CreateLessonRequest(
                "Test",
                "Description",
                10,
                LessonStatus.PUBLISHED
        );

        Lesson lesson = mapper.toEntity(request, moduleId);

        Assertions.assertNotNull(lesson);
        Assertions.assertEquals(moduleId, lesson.getModuleId());
        Assertions.assertEquals("Test", lesson.getName());
        Assertions.assertEquals("Description", lesson.getDescription());
        Assertions.assertEquals(10, lesson.getDuration());
        Assertions.assertEquals(LessonStatus.PUBLISHED, lesson.getStatus());
    }

    @Test
    @DisplayName("Func updateEntity should map all fields")
    void updateEntity_shouldMapAllFields() {
        Long moduleId = 1L;

        Lesson lesson = Lesson.builder()
                .moduleId(moduleId)
                .name("Test")
                .description("Description")
                .duration(10)
                .position(1)
                .status(LessonStatus.PUBLISHED)
                .build();

        UpdateLessonRequest request = new UpdateLessonRequest(
                "Updated",
                "Updated description",
                20,
                LessonStatus.ARCHIVED
        );

        mapper.updateEntity(lesson, request);

        Assertions.assertNotNull(lesson);
        Assertions.assertEquals(moduleId, lesson.getModuleId());
        Assertions.assertEquals("Updated", lesson.getName());
        Assertions.assertEquals("Updated description", lesson.getDescription());
        Assertions.assertEquals(20, lesson.getDuration());
        Assertions.assertEquals(LessonStatus.ARCHIVED, lesson.getStatus());
    }

    @Test
    @DisplayName("Func toResponse should map all fields")
    void toResponse_shouldMapAllFields() {
        Long moduleId = 1L;

        Lesson lesson = Lesson.builder()
                .moduleId(moduleId)
                .name("Test")
                .description("Description")
                .duration(10)
                .position(1)
                .status(LessonStatus.PUBLISHED)
                .build();

        LessonResponse response =  mapper.toResponse(lesson);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(lesson.getName(), response.name());
        Assertions.assertEquals(lesson.getDescription(), response.description());
        Assertions.assertEquals(lesson.getDuration(), response.duration());
        Assertions.assertEquals(lesson.getPosition(), response.position());
        Assertions.assertEquals(lesson.getStatus(), response.status());
    }

    @Test
    @DisplayName("Func toShortResponse should map all fields")
    void toShortResponse_shouldMapAllFields() {
        Long moduleId = 1L;

        Lesson lesson = Lesson.builder()
                .moduleId(moduleId)
                .name("Test")
                .description("Description")
                .duration(10)
                .position(1)
                .status(LessonStatus.PUBLISHED)
                .build();

        LessonShortResponse response =  mapper.toShortResponse(lesson);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(lesson.getModuleId(), response.moduleId());
        Assertions.assertEquals(lesson.getName(), response.name());
        Assertions.assertEquals(lesson.getStatus(), response.status());
    }

}
