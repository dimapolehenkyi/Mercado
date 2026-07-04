package com.example.mercado.testUtils.courses.module;

import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;

public class ModuleTestData {

    public static ModuleResponse mapToResponse(
            Module module
    ) {
        return new ModuleResponse(
                module.getId(),
                module.getCourseId(),
                module.getName(),
                module.getDescription(),
                module.isDeleted(),
                module.getPosition(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }

    public static ModuleShortResponse mapToShortResponse(
            Module module
    ) {
        return new ModuleShortResponse(
                module.getId(),
                module.getCourseId(),
                module.getName(),
                module.getPosition()
        );
    }

}
