package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateCourseRequest(

        @Size(
                min = 1,
                max = 100,
                message = "Name must not be empty"
        )
        String name,

        @Size(
                max = 2000,
                message = "Description too long"
        )
        String description,

        @Size(
                max = 500,
                message = "Short description too long"
        )
        String shortDescription,

        CourseAccessType type,
        CourseLevel level,

        BigDecimal price,

        String previewVideoUrl,
        String thumbnailUrl

) {
}
