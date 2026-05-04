package com.example.mercado.testUtils.courses.course;

import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Helper-class for testing package of Course
 */
public class CourseTestFactory {

    /**
     * Function for fast creating Course-entity
     * where you can customize parameter's
     * @param consumer
     * @return Course
     */
    public static Course createCourse(
            Consumer<Course.CourseBuilder> consumer
    ) {
        Course.CourseBuilder builder = Course.builder()
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
        consumer.accept(builder);
        return builder.build();
    }

    /**
     * Function for fast creating "CreateCourseRequest"
     * where you can customize parameters
     * @param customizer
     * @return CreateCourseRequest
     */
    public static CreateCourseRequest createCourseRequest(
            Consumer<Args> customizer
    ) {
        Args args = new Args();
        customizer.accept(args);
        return new CreateCourseRequest(
                args.name,
                args.description,
                args.shortDescription,
                args.type,
                args.level,
                args.price,
                args.previewVideoUrl,
                args.thumbnailUrl
        );
    }

    /**
     * Function for fast creating "UpdateCourseRequest"
     * where you can customize parameters
     * @param customizer
     * @return UpdateCourseRequest
     */
    public static UpdateCourseRequest updateCourseRequest(
            Consumer<Args> customizer
    ) {
        Args args = new Args();

        args.name = "New name";
        args.description = "New description";
        args.shortDescription = "New short description";
        args.type = CourseAccessType.PAID;
        args.level = CourseLevel.ADVANCED;
        args.price = BigDecimal.valueOf(200L);
        args.previewVideoUrl = "https://newPreviewVideoUrl";
        args.thumbnailUrl = "https://newThumbnailUrl";

        customizer.accept(args);
        return new UpdateCourseRequest(
                args.name,
                args.description,
                args.shortDescription,
                args.type,
                args.level,
                args.price,
                args.previewVideoUrl,
                args.thumbnailUrl
        );
    }

    public static class Args {
        public String name = "Test course";
        public String description = "Test description";
        public String shortDescription = "Test short description";
        public CourseAccessType type = CourseAccessType.PAID;
        public CourseLevel level = CourseLevel.ADVANCED;
        public BigDecimal price = BigDecimal.valueOf(100);
        public String previewVideoUrl = "https://previewVideoUrl";
        public String thumbnailUrl = "https://thumbnailUrl";
    }

}
