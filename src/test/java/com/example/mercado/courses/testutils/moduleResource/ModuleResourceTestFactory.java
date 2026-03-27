package com.example.mercado.courses.testutils.moduleResource;

import com.example.mercado.courses.moduleResource.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.entity.ModuleResource;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;

public class ModuleResourceTestFactory {

    public static ModuleResource createModuleResource(
            Long moduleId, int position, String name, ModuleResourceType type
    ) {
        return ModuleResource.builder()
                .moduleId(moduleId)
                .name(name)
                .type(type)
                .url("https://url")
                .thumbnailUrl("https://thumbnailUrl")
                .position(position)
                .build();
    }

    public static CreateModuleResourceRequest createModuleResourceRequest() {
        return new CreateModuleResourceRequest(
                "Test",
                ModuleResourceType.MP4,
                "https://url",
                "https://thumbnailUrl"
        );
    }

    public static UpdateModuleResourceRequest updateModuleResourceRequest(
            Long moduleId, String name
    ) {
        return new UpdateModuleResourceRequest(
                moduleId,
                name,
                ModuleResourceType.MP3,
                "https://updatedUrl",
                "https://updatedThumbnailUrl"
        );
    }
}
