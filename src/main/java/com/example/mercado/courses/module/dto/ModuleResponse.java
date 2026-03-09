package com.example.mercado.courses.module.dto;

import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;

import java.time.LocalDateTime;

public record ModuleResponse(

        Long id,

        Long courseId,

        String name,

        String description,

        ModuleStatus status,

        ModuleType moduleType,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
