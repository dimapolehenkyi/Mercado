package com.example.mercado.courses.moduleResource.mapper;

import com.example.mercado.courses.moduleResource.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleResource.entity.ModuleResource;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Module Resource Mapper Test")
public class ModuleResourceMapperTest {

    private ModuleResourceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ModuleResourceMapper();
    }


    @Test
    @DisplayName("Func toEntity should map all fields")
    void toEntity_shouldMapAllFields() {
        Long moduleId = 1L;

        CreateModuleResourceRequest request = new CreateModuleResourceRequest(
                "Java",
                ModuleResourceType.MP4,
                "https://www.mp4.url",
                "https://www.mp4.thumbnailUrl"
        );

        ModuleResource entity = mapper.toEntity(moduleId, request);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals("Java", entity.getName());
        Assertions.assertEquals(ModuleResourceType.MP4, entity.getType());
        Assertions.assertEquals("https://www.mp4.url", entity.getUrl());
        Assertions.assertEquals("https://www.mp4.thumbnailUrl", entity.getThumbnailUrl());
    }

    @Test
    @DisplayName("Func toResponse should map all fields")
    void toResponse_shouldMapAllFields() {
        ModuleResource entity = ModuleResource.builder()
                .id(1L)
                .moduleId(1L)
                .name("Java")
                .type(ModuleResourceType.MP4)
                .url("https://www.mp4.url")
                .thumbnailUrl("https://www.mp4.thumbnailUrl")
                .position(1)
                .build();

        ModuleResourceResponse response = mapper.toResponse(entity);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.id(), entity.getId());
        Assertions.assertEquals(response.moduleId(), entity.getModuleId());
        Assertions.assertEquals(response.name(), entity.getName());
        Assertions.assertEquals(response.type(), entity.getType());
        Assertions.assertEquals(response.url(), entity.getUrl());
        Assertions.assertEquals(response.thumbnailUrl(), entity.getThumbnailUrl());
        Assertions.assertEquals(response.position(), entity.getPosition());
    }


}
