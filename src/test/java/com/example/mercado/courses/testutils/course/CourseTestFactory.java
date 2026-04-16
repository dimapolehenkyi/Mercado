package com.example.mercado.courses.testutils.course;


import com.example.mercado.courses.course.dto.ChangeLevelRequest;
import com.example.mercado.courses.course.dto.ChangeStatusRequest;
import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;

public class CourseTestFactory {

    public static Course.CourseBuilder createDefaultCourse() {
        return Course.builder()
                .name("Test course")
                .description("Test description")
                .shortDescription("Test short description")
                .status(CourseStatus.PUBLISHED)
                .type(CourseAccessType.PAID)
                .isFree(false)
                .price(BigDecimal.valueOf(100L))
                .level(CourseLevel.ADVANCED)
                .rating(0.0)
                .previewVideoUrl("https://previewVideoUrl")
                .thumbnailUrl("https://thumbnailUrl")
                .deleted(false);
    }

    public static CreateCourseRequest defaultCreateCourseRequest() {
        return new CreateCourseRequest(
                "Java",
                "Test description",
                "Test short description",
                CourseAccessType.PAID,
                CourseLevel.ADVANCED,
                BigDecimal.valueOf(100L),
                "https://previewVideoUrl",
                "https://thumbnailUrl"
        );
    }

    public static CreateCourseRequest createCourseRequestWithBlankName() {
        return new CreateCourseRequest(
                "",
                "Test description",
                "Test short description",
                CourseAccessType.PAID,
                CourseLevel.ADVANCED,
                BigDecimal.valueOf(100L),
                "https://previewVideoUrl",
                "https://thumbnailUrl"
        );
    }

    public static CreateCourseRequest createCourseRequestWithNullType() {
        return new CreateCourseRequest(
                "",
                "Test description",
                "Test short description",
                null,
                CourseLevel.ADVANCED,
                BigDecimal.valueOf(100L),
                "https://previewVideoUrl",
                "https://thumbnailUrl"
        );
    }

    public static CreateCourseRequest createCourseRequestWithNullLevel() {
        return new CreateCourseRequest(
                "",
                "Test description",
                "Test short description",
                CourseAccessType.PAID,
                null,
                BigDecimal.valueOf(100L),
                "https://previewVideoUrl",
                "https://thumbnailUrl"
        );
    }

    public static CreateCourseRequest createCourseRequestWithNullPrice() {
        return new CreateCourseRequest(
                "",
                "Test description",
                "Test short description",
                CourseAccessType.PAID,
                CourseLevel.ADVANCED,
                null,
                "https://previewVideoUrl",
                "https://thumbnailUrl"
        );
    }

    public static UpdateCourseRequest defaultUpdateCourseRequest() {
        return new UpdateCourseRequest(
                "New name",
                "New description",
                "New short description",
                CourseAccessType.PAID,
                CourseLevel.ADVANCED,
                BigDecimal.valueOf(200L),
                "https://NewPreviewVideoUrl",
                "https://NewThumbnailUrl"
        );
    }

    public static UpdateCourseRequest customUpdateCourseRequestWithNull(
            String name,
            CourseAccessType type,
            CourseLevel level,
            BigDecimal price
    ) {
        return new UpdateCourseRequest(
                name,
                null,
                null,
                type,
                level,
                price,
                null,
                null
        );
    }

    public static ChangeStatusRequest customChangeStatusRequest(CourseStatus status) {
        return new ChangeStatusRequest(
                status
        );
    }

    public static ChangeLevelRequest customChangeLevelRequest(CourseLevel level) {
        return new ChangeLevelRequest(
                level
        );
    }

}
