package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.customValidators.price.validPrice.ValidPrice;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@ValidPrice
public record CreateCourseRequest(


        @NotBlank(
                message = "Name is required"
        )
        String name,

        String description,
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
