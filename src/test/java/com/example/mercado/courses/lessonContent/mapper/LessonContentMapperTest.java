package com.example.mercado.courses.lessonContent.mapper;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.entity.LessonContent;
import com.example.mercado.courses.lessonContent.enums.LessonContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LessonContentMapperTest {

    private LessonContentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LessonContentMapper();
    }

    @Test
    void toEntity_shouldMapAllFields() {
        Long lessonId = 1L;
        CreateLessonContentRequest request = new CreateLessonContentRequest(
                "Lesson Name",
                "Description of lesson",
                LessonContentType.MP4,
                "http://example.com/video.mp4",
                "http://example.com/thumb.jpg"

        );

        LessonContent entity = mapper.toEntity(lessonId, request);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getLessonId()).isEqualTo(lessonId);
        Assertions.assertThat(entity.getName()).isEqualTo("Lesson Name");
        Assertions.assertThat(entity.getDescription()).isEqualTo("Description of lesson");
        Assertions.assertThat(entity.getUrl()).isEqualTo("http://example.com/video.mp4");
        Assertions.assertThat(entity.getThumbnailUrl()).isEqualTo("http://example.com/thumb.jpg");
        Assertions.assertThat(entity.getType()).isEqualTo(LessonContentType.MP4);
    }

    @Test
    void toResponse_shouldMapAllFields() {
        LessonContent entity = LessonContent.builder()
                .id(10L)
                .lessonId(1L)
                .name("Lesson Name")
                .description("Description of lesson")
                .position(3)
                .url("http://example.com/video.mp4")
                .thumbnailUrl("http://example.com/thumb.jpg")
                .type(LessonContentType.MP4)
                .build();

        LessonContentResponse response = mapper.toResponse(entity);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.id()).isEqualTo(10L);
        Assertions.assertThat(response.lessonId()).isEqualTo(1L);
        Assertions.assertThat(response.name()).isEqualTo("Lesson Name");
        Assertions.assertThat(response.description()).isEqualTo("Description of lesson");
        Assertions.assertThat(response.position()).isEqualTo(3);
        Assertions.assertThat(response.url()).isEqualTo("http://example.com/video.mp4");
        Assertions.assertThat(response.thumbnailUrl()).isEqualTo("http://example.com/thumb.jpg");
        Assertions.assertThat(response.type()).isEqualTo(LessonContentType.MP4);
    }

}
