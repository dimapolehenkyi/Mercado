package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;

public record CreateCourseRequest(

        Long teacherId,
        String name,
        String description,
        CourseAccessType type,
        BigDecimal price,
        Integer durationInMinutes,
        CourseStatus status

) {
}
