package com.example.mercado.courses.testutils.course;


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

}
