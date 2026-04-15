package com.example.mercado.courses.moduleContent.dto;

import com.example.mercado.courses.moduleContent.enums.ModuleResourceType;

public record UpdateModuleResourceRequest(

        Long moduleId,

        String name,

        ModuleResourceType type,

        String url,

        String thumbnailUrl

) {
}
