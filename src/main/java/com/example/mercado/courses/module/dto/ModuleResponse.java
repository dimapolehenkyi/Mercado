package com.example.mercado.courses.module.dto;

import java.time.LocalDateTime;

public record ModuleResponse(

        Long id,

        Long courseId,

        String name,

        String description,

        boolean deleted,

        Integer position,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
