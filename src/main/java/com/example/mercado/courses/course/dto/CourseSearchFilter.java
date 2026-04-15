package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.customValidators.price.validPriceRange.ValidPriceRange;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.SortType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@ValidPriceRange
public record CourseSearchFilter(

        @Size(
                max = 100
        )
        @Pattern(
                regexp = "^[a-zA-Z0-9\\s]*$",
                message = "Invalid keyword"
        )
        String keyword,

        CourseAccessType type,

        @Positive
        Long teacherId,

        @PositiveOrZero
        BigDecimal priceFrom,

        @PositiveOrZero
        BigDecimal priceTo,

        CourseLevel level,

        SortType sortType,

        Boolean isFree
) {
}
