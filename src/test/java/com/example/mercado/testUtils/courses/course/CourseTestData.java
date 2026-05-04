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
import java.util.function.Consumer;

public class CourseTestData {

    public static CourseDetailsResponse courseDetails(
            Consumer<DetailsArgs> customizer
    ) {
        DetailsArgs a = new DetailsArgs();
        customizer.accept(a);

        return new CourseDetailsResponse(
                a.id,
                a.userId,
                a.teacherId,
                a.name,
                a.description,
                a.shortDescription,
                a.previewVideoUrl,
                a.thumbnailUrl,
                a.studentCount,
                a.rating,
                a.reviewsCount,
                a.price,
                a.status,
                a.type,
                a.level,
                a.durationInMinutes,
                a.deleted,
                a.isFree,
                a.createdAt,
                a.updatedAt
        );
    }

    public static class DetailsArgs {
        public Long id = 1L;
        public Long userId = 1L;
        public Long teacherId = 1L;
        public String name = "Test course";
        public String description = "description";
        public String shortDescription = "short description";
        public String previewVideoUrl = "previewUrl";
        public String thumbnailUrl = "thumbnailUrl";
        public Long studentCount = 10L;
        public Double rating = 4.5;
        public Long reviewsCount = 5L;
        public BigDecimal price = BigDecimal.valueOf(100);
        public CourseStatus status = CourseStatus.PUBLISHED;
        public CourseAccessType type = CourseAccessType.PAID;
        public CourseLevel level = CourseLevel.BEGINNER;
        public Integer durationInMinutes = 120;
        public Boolean deleted = false;
        public Boolean isFree = false;
        public LocalDateTime createdAt = LocalDateTime.now();
        public LocalDateTime updatedAt = LocalDateTime.now();
    }

    public static CourseShortResponse courseShort(
            Consumer<ShortArgs> customizer
    ) {
        ShortArgs a = new ShortArgs();
        customizer.accept(a);

        return new CourseShortResponse(
                a.id,
                a.name,
                a.shortDescription,
                a.price,
                a.type,
                a.status,
                a.rating,
                a.thumbnailUrl
        );
    }

    public static class ShortArgs {
        public Long id = 1L;
        public String name = "Test course";
        public String shortDescription = "short description";
        public BigDecimal price = BigDecimal.valueOf(100);
        public CourseAccessType type = CourseAccessType.PAID;
        public CourseStatus status = CourseStatus.PUBLISHED;
        public Double rating = 0.0;
        public String thumbnailUrl = "https://thumbnailUrl";
    }


    public static Page<Course> page(Course... courses) {
        return new PageImpl<>(List.of(courses));
    }

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

}
