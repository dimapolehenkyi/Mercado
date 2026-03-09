package com.example.mercado.courses.module.dto;

import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

public record CreateModuleRequest(

        @NotBlank
        String name,

        @Size(min = 1, max = 1000)
        String description,

        ModuleStatus moduleStatus,

        ModuleType moduleType

) {
}
