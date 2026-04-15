package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;

public record CourseShortResponse(

        Long id,

        String name,

        String shortDescription,

        BigDecimal price,

        CourseAccessType type,

        Boolean deleted,

        CourseStatus status,

        Double rating,

        String thumbnailUrl

) {
}
