package com.example.mercado.testUtils.courses.course;

import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CourseTestData {

    public static CourseShortResponse mapToShortResponse(Course c) {
        return new CourseShortResponse(
                c.getId(),
                c.getName(),
                c.getShortDescription(),
                c.getPrice(),
                c.getType(),
                c.getStatus(),
                c.getRating(),
                c.getThumbnailUrl()
        );
    }

    public static CourseDetailsResponse mapToDetailsResponse(Course c) {
        return new CourseDetailsResponse(
                c.getId(),
                c.getUserId(),
                c.getTeacherId(),
                c.getName(),
                c.getDescription(),
                c.getShortDescription(),
                c.getPreviewVideoUrl(),
                c.getThumbnailUrl(),
                c.getStudentCount(),
                c.getRating(),
                c.getReviewsCount(),
                c.getPrice(),
                c.getStatus(),
                c.getType(),
                c.getLevel(),
                c.getDurationInMinutes(),
                c.getDeleted(),
                c.getIsFree(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }

    public static CourseDetailsResponse courseDetailsResponse(Long id, String name) {
        return new CourseDetailsResponse(
                id,
                1L,
                1L,
                name,
                "description",
                "short description",
                "previewUrl",
                "thumbnailUrl",
                10L,
                4.5,
                5L,
                BigDecimal.valueOf(100),
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID,
                CourseLevel.BEGINNER,
                120,
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static CourseDetailsResponse courseUpdatedDetailsResponse(Long id, String name) {
        return new CourseDetailsResponse(
                id,
                1L,
                1L,
                name,
                "New description",
                "New short description",
                "https://NewPreviewVideoUrl",
                "https://NewThumbnailUrl",
                10L,
                4.5,
                5L,
                BigDecimal.valueOf(200L),
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID,
                CourseLevel.ADVANCED,
                120,
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static CourseDetailsResponse courseUpdatedDetailsResponseWithNull(
            Long id,
            String name,
            CourseAccessType type,
            CourseLevel level,
            BigDecimal price
    ) {
        return new CourseDetailsResponse(
                id,
                1L,
                1L,
                name,
                "New description",
                "New short description",
                "https://NewPreviewVideoUrl",
                "https://NewThumbnailUrl",
                10L,
                4.5,
                5L,
                price,
                CourseStatus.PUBLISHED,
                type,
                level,
                120,
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static CourseShortResponse courseShortResponse(Long id, String name) {
        return new CourseShortResponse(
                id,
                name,
                "short description",
                BigDecimal.valueOf(100),
                CourseAccessType.PAID,
                CourseStatus.PUBLISHED,
                0.0,
                "https://thumbnailUrl"
        );
    }

    public static Page<Course> page(Course... courses) {
        return new PageImpl<>(List.of(courses));
    }

}
