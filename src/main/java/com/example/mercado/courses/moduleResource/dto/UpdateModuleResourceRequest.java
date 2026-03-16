package com.example.mercado.courses.moduleResource.dto;

import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;

public record UpdateModuleResourceRequest(

        Long moduleId,

        String name,

        ModuleResourceType type,

        String url,

        String thumbnailUrl

) {
}
