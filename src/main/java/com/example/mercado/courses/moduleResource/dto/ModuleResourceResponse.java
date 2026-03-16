package com.example.mercado.courses.moduleResource.dto;

import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;

public record ModuleResourceResponse(

        Long id,

        Long moduleId,

        String name,

        ModuleResourceType type,

        String url,

        String thumbnailUrl,

        Integer position

) {
}
