package com.example.mercado.courses.module.mapper;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public Module toEntity(@NonNull CreateModuleRequest request, Long courseId) {
        return Module.builder()
                .courseId(courseId)
                .name(request.name())
                .description(request.description())
                .status(request.moduleStatus())
                .moduleType(request.moduleType())
                .build();
    }

    public void updateEntity(@NonNull Module module, @NonNull UpdateModuleRequest request) {
        module.setName(request.name());
        module.setDescription(request.description());
        module.setModuleType(request.moduleType());
        module.setStatus(request.status());
    }

    public ModuleResponse toResponse(@NonNull Module module) {
        return new ModuleResponse(
                module.getId(),
                module.getCourseId(),
                module.getName(),
                module.getDescription(),
                module.getStatus(),
                module.getModuleType(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }

    public ModuleShortResponse toShortResponse(@NonNull Module module) {
        return new ModuleShortResponse(
                module.getId(),
                module.getName(),
                module.getStatus()
        );
    }

}
