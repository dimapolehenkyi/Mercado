package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;

import java.math.BigDecimal;

public record CourseSearchFilter(
        String keyword,
        CourseAccessType type,
        Long teacherId,
        BigDecimal priceFrom,
        BigDecimal priceTo
) {
}
