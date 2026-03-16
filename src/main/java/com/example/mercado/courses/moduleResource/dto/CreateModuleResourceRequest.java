package com.example.mercado.courses.moduleResource.dto;

import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;

public record CreateModuleResourceRequest(

        String name,

        ModuleResourceType type,

        String url,

        String thumbnailUrl

) {
}
