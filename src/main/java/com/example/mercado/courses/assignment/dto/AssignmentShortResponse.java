package com.example.mercado.courses.assignment.dto;

import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;

public record AssignmentShortResponse(

        Long id,

        String name,

        AssignmentType type,

        AssignmentStatus status,

        Integer maxScore,

        Integer position

) {
}
