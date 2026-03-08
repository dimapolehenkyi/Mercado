package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseResponse(

        Long id,
        Long teacherId,
        String name,
        String description,
        BigDecimal price,
        CourseStatus status,
        CourseAccessType type,
        Integer durationInMinutes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
