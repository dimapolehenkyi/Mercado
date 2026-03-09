package com.example.mercado.courses.course.mapper;

import com.example.mercado.courses.course.dto.CourseResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(@NonNull CreateCourseRequest request) {
        return Course.builder()
                .teacherId(request.teacherId())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .durationInMinutes(request.durationInMinutes())
                .status(request.status())
                .type(request.type())
                .build();
    }

    public void updateEntity(Course course, @NonNull UpdateCourseRequest request) {
        course.setName(request.name());
        course.setDescription(request.description());
        course.setStatus(request.status());
        course.setPrice(request.price());
        course.setType(request.type());
    }

    public CourseResponse toResponse(@NonNull Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTeacherId(),
                course.getName(),
                course.getDescription(),
                course.getPrice(),
                course.getStatus(),
                course.getType(),
                course.getDurationInMinutes(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }

    public CourseShortResponse toShortResponse(@NonNull Course course) {
        return new CourseShortResponse(
                course.getId(),
                course.getName(),
                course.getPrice(),
                course.getType(),
                course.getDurationInMinutes(),
                course.getStatus()
        );
    }
}
