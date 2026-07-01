package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCourseRequest(


        @NotBlank(
                message = "Name is required"
        )
        @Pattern(
                regexp = "^[a-zA-Z0-9\\s]*$",
                message = "Invalid keyword"
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

        @NotNull(
                message = "Course type is required"
        )
        CourseAccessType type,

        @NotNull(
                message = "Course level is required"
        )
        CourseLevel level,

        @NotNull(
                message = "Price is required"
        )
        BigDecimal price,

        String previewVideoUrl,
        String thumbnailUrl
) {
}
