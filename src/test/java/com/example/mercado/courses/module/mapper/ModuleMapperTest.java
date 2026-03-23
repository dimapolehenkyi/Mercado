package com.example.mercado.courses.module.mapper;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import com.example.mercado.courses.testutils.module.ModuleTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ModuleMapper Test")
public class ModuleMapperTest {

    private ModuleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ModuleMapper();
    }



    @Test
    @DisplayName("Func toEntity should map all fields to entity type")
    void toEntity_shouldMapAllFields() {
        Long courseId = 1L;

        CreateModuleRequest request = ModuleTestFactory.createModuleRequest();

        Module mapped = mapper.toEntity(request, courseId);

        Assertions.assertNotNull(mapped);
        Assertions.assertEquals(courseId, mapped.getCourseId());
        Assertions.assertEquals(request.name(), mapped.getName());
        Assertions.assertEquals(request.description(), mapped.getDescription());
        Assertions.assertEquals(request.moduleStatus(), mapped.getStatus());
        Assertions.assertEquals(request.moduleType(), mapped.getModuleType());
    }

    @Test
    @DisplayName("Func updateEntity should map fields to new value")
    void updateEntity_shouldMapFields_toNewValue() {
        Module module = createModule(1L, "Multithreading", ModuleStatus.ARCHIVED, ModuleType.FREE);

        UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest();

        mapper.updateEntity(module, request);

        Assertions.assertNotNull(module);
        Assertions.assertEquals(1L, module.getCourseId());
        Assertions.assertEquals("Updated module", module.getName());
        Assertions.assertEquals("Updated description", module.getDescription());
        Assertions.assertEquals(ModuleStatus.PUBLISHED, module.getStatus());
        Assertions.assertEquals(ModuleType.PAID, module.getModuleType());
    }

    @Test
    @DisplayName("Func toResponse should map all fields to response type")
    void toResponse_shouldMapAllFields() {
        Module module = createModule(2L, "Java Core", ModuleStatus.PUBLISHED, ModuleType.PAID);

        ModuleResponse response = mapper.toResponse(module);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2L, module.getCourseId());
        Assertions.assertEquals("Java Core", module.getName());
        Assertions.assertEquals("Test description", module.getDescription());
        Assertions.assertEquals(ModuleStatus.PUBLISHED, module.getStatus());
        Assertions.assertEquals(ModuleType.PAID, module.getModuleType());
    }

    @Test
    @DisplayName("Func toShortResponse should map all fields to short response type")
    void toShortResponse_shouldMapAllFields() {
        Module module = createModule(3L, "Python Core", ModuleStatus.ARCHIVED, ModuleType.PAID);

        ModuleShortResponse shortResponse = mapper.toShortResponse(module);

        Assertions.assertNotNull(shortResponse);
        Assertions.assertEquals("Python Core", module.getName());
        Assertions.assertEquals(ModuleStatus.ARCHIVED, module.getStatus());
    }



    private Module createModule(
            Long courseId,
            String name,
            ModuleStatus status,
            ModuleType type
    ) {
        return ModuleTestFactory.createModule(courseId, name, status, type);
    }

}
