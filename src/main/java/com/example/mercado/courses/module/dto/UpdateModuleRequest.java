package com.example.mercado.courses.module.dto;

import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleAccessType;

public record UpdateModuleRequest(

        String name,

        String description

) {
}
