package com.example.mercado.courses.assignment.dto;

import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;

public record AssignmentResponse(

        Long id,

        Long lessonId,

        String name,

        String description,

        AssignmentType type,

        AssignmentStatus status,

        Integer maxScore,

        Integer position

) {
}
