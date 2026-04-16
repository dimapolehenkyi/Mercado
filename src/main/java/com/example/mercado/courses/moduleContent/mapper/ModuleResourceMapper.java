package com.example.mercado.courses.moduleContent.mapper;

import com.example.mercado.courses.moduleContent.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleContent.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleContent.entity.ModuleResource;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ModuleResourceMapper {

    public ModuleResource toEntity(@NonNull Long moduleId, @NonNull CreateModuleResourceRequest request) {
        return ModuleResource.builder()
                .moduleId(moduleId)
                .name(request.name())
                .type(request.type())
                .url(request.url())
                .thumbnailUrl(request.thumbnailUrl())
                .build();
    }

    public ModuleResourceResponse toResponse(@NonNull ModuleResource entity) {
        return new  ModuleResourceResponse(
                entity.getId(),
                entity.getModuleId(),
                entity.getName(),
                entity.getType(),
                entity.getUrl(),
                entity.getThumbnailUrl(),
                entity.getPosition()
        );
    }

}
