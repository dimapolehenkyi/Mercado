package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;

public record UpdateCourseRequest(

        String name,
        String description,
        BigDecimal price,
        CourseStatus status,
        CourseAccessType type

) {
}
