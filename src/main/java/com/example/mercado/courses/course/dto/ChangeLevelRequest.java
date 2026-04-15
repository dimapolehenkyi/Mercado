package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseLevel;
import jakarta.validation.constraints.NotNull;

public record ChangeLevelRequest(

        @NotNull(
                message = "Course level is required"
        )
        CourseLevel level

) {
}
