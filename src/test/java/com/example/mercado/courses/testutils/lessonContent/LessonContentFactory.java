package com.example.mercado.courses.testutils.lessonContent;

import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;
import com.example.mercado.courses.lessonContent.entity.LessonContent;
import com.example.mercado.courses.lessonContent.enums.LessonContentType;

public class LessonContentFactory {

    public static LessonContent createLessonContent() {
        LessonContent lessonContent = new LessonContent();
        lessonContent.setName("Test");
        lessonContent.setDescription("Test description");
        lessonContent.setUrl("url");
        lessonContent.setThumbnailUrl("thumbnailUrl");
        lessonContent.setLessonId(1L);
        lessonContent.setPosition(1);

        return lessonContent;
    }

    public static CreateLessonContentRequest createCreateLessonContentRequest() {
        return new CreateLessonContentRequest(
                "Test",
                "Test description",
                LessonContentType.MP4,
                "Test url",
                "Test thumbnailUrl"
        );
    }

    public static CreateLessonContentRequest createCreateLessonContentRequest(String name) {
        return new CreateLessonContentRequest(
                name,
                "Test description",
                LessonContentType.MP4,
                "Test url",
                "Test thumbnailUrl"
        );
    }

    public static LessonContentResponse createLessonContentResponse() {
        return new LessonContentResponse(
                1L,
                1L,
                "Test",
                "Test description",
                1,
                "Test url",
                "Test thumbnailUrl",
                LessonContentType.MP4
        );
    }

    public static UpdateLessonContentRequest updateLessonContentResponse() {
        return new UpdateLessonContentRequest(
                "New name",
                "New description",
                LessonContentType.MP3,
                "New url",
                "New thumbnailUrl"
        );
    }

}
