package com.example.mercado.unit.courses.module.mapper;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;

@DisplayName("Module Mapper Test")
public class ModuleMapperTest {

    private final ModuleMapper mapper = Mappers.getMapper(ModuleMapper.class);

    @Test
    void toEntity_shouldMapFieldsCorrectly() {
        CreateModuleRequest request = ModuleTestFactory.createModuleRequest(
                a -> {
                    a.name = "Test module";
                    a.description = "Test description";
                }
        );

        Module module = mapper.toEntity(request, 1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test module", module.getName()),
                () -> Assertions.assertEquals("Test description", module.getDescription()),
                () -> Assertions.assertEquals(1L, module.getCourseId()),
                () -> Assertions.assertFalse(module.isDeleted())
        );
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            " ",
            "   ",
            "\t",
            "\n"
    })
    void toEntity_shouldThrow_whenNameIsInvalid(
            String name
    ) {
        CreateModuleRequest request = ModuleTestFactory.createModuleRequest(
                a -> {
                    a.name = name;
                    a.description = "Test description";
                }
        );

        Assertions.assertThrows(AppException.class, () -> {
            mapper.toEntity(request, 1L);
        });
    }

    @Test
    void toResponse_shouldMapRealValues() {
        Module module = ModuleTestFactory.createModule(m -> {
            m.setName("Test module");
            m.setDescription("Test description");
            m.setCourseId(1L);
            m.setDeleted(false);
            m.setPosition(1);
        });

        ModuleResponse moduleResponse = mapper.toResponse(module);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test module", moduleResponse.name()),
                () -> Assertions.assertEquals("Test description", moduleResponse.description()),
                () -> Assertions.assertEquals(1L, moduleResponse.courseId()),
                () -> Assertions.assertFalse(moduleResponse.deleted()),
                () -> Assertions.assertEquals(1, moduleResponse.position())
        );
    }

    @Test
    void toShortResponse_shouldMapRealValues() {
        Module module = ModuleTestFactory.createModule(m -> {
            m.setName("Test module");
            m.setDescription("Test description");
            m.setCourseId(1L);
            m.setDeleted(false);
            m.setPosition(1);
        });

        ModuleShortResponse moduleShortResponse = mapper.toShortResponse(module);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test module", moduleShortResponse.name()),
                () -> Assertions.assertEquals(1L, moduleShortResponse.courseId()),
                () -> Assertions.assertEquals(1, moduleShortResponse.position())
        );
    }

}
