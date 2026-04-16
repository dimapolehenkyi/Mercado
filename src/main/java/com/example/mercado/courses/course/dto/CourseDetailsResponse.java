package com.example.mercado.courses.course.dto;

import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseDetailsResponse(

        Long id,

        Long userId,

        Long teacherId,

        String name,

        String description,

        String shortDescription,

        String previewVideoUrl,

        String thumbnailUrl,

        Long studentCount,

        Double rating,

        Long reviewsCount,

        BigDecimal price,

        CourseStatus status,

        CourseAccessType type,

        CourseLevel level,

        Integer durationInMinutes,

        Boolean deleted,

        Boolean isFree,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
