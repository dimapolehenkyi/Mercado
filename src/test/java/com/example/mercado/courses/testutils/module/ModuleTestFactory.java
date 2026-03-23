package com.example.mercado.courses.testutils.module;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import com.example.mercado.courses.module.entity.Module;

public class ModuleTestFactory {

    public static Module createModule(
            Long courseId,
            String name,
            ModuleStatus status,
            ModuleType type
    ) {
        Module module = new Module();
        module.setCourseId(courseId);
        module.setName(name);
        module.setDescription("Test description");
        module.setStatus(status);
        module.setModuleType(type);
        module.setDeleted(false);
        module.setPosition(1);

        return module;
    }

    public static CreateModuleRequest createModuleRequest() {
        return new CreateModuleRequest(
                "Test module",
                "Test description",
                ModuleStatus.PUBLISHED,
                ModuleType.FREE
        );
    }

    public static UpdateModuleRequest updateModuleRequest() {
        return new UpdateModuleRequest(
                "Updated module",
                "Updated description",
                ModuleStatus.PUBLISHED,
                ModuleType.PAID
        );
    }

    public static ModuleResponse createResponse() {
        return new ModuleResponse(
                1L,
                1L,
                "Java Core",
                "Test description",
                ModuleStatus.PUBLISHED,
                ModuleType.PAID,
                null,
                null
        );
    }

}
