package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(

        @NotNull(
                message = "Course status is required"
        )
        CourseStatus status

) {
}
