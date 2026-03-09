package com.example.mercado.courses.module.dto;

import com.example.mercado.courses.module.enums.ModuleStatus;

public record ModuleShortResponse(

        Long moduleId,

        String name,

        ModuleStatus status

) {
}
