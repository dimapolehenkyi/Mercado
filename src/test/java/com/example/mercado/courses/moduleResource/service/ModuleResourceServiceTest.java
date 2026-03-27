package com.example.mercado.courses.moduleResource.service;

import com.example.mercado.courses.moduleResource.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleResource.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.entity.ModuleResource;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;
import com.example.mercado.courses.moduleResource.mapper.ModuleResourceMapper;
import com.example.mercado.courses.moduleResource.repository.ModuleResourceRepository;
import com.example.mercado.courses.testutils.moduleResource.ModuleResourceTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModuleResourceService Test")
public class ModuleResourceServiceTest {

    @Mock
    private ModuleResourceMapper mapper;

    @Mock
    private ModuleResourceRepository repository;

    @InjectMocks
    private ModuleResourceServiceImpl service;


    private ModuleResource moduleResource;
    private CreateModuleResourceRequest createModuleResourceRequest;
    private UpdateModuleResourceRequest updateModuleResourceRequest;
    private ModuleResourceResponse moduleResourceResponse;


    @BeforeEach
    void setUp() {
        moduleResource = ModuleResourceTestFactory.createModuleResource(
                1L, 1, "Test", ModuleResourceType.MP4
        );
        moduleResourceResponse = new ModuleResourceResponse(
                1L,
                1L,
                "Test",
                ModuleResourceType.MP4,
                "https://url",
                "https://thumbnailUrl",
                1
        );
    }

    @Test
    @DisplayName("Func createModuleResource should create ModuleResource and return response")
    void createModuleResource_shouldCreateModuleResource_andReturnResponse() {
        Long  moduleId = 1L;
        CreateModuleResourceRequest request = ModuleResourceTestFactory.createModuleResourceRequest();

        Mockito.when(repository.existsByModuleIdAndNameAndType(moduleId, request.name(),  request.type())).thenReturn(false);
        Mockito.when(mapper.toEntity(moduleId, request)).thenReturn(moduleResource);
        Mockito.when(mapper.toResponse(moduleResource)).thenReturn(moduleResourceResponse);

        ModuleResourceResponse response = service.createModuleResource(moduleId, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(moduleResourceResponse, response);

        Mockito.verify(repository, Mockito.times(1)).existsByModuleIdAndNameAndType(moduleId, request.name(), request.type());
        Mockito.verify(mapper, Mockito.times(1)).toEntity(moduleId, request);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(Mockito.any(ModuleResource.class));
    }

    @Test
    @DisplayName("Func updateModuleResource should update ModuleResource and return response")
    void updateModuleResource_shouldUpdateModuleResource_andReturnResponse() {
        Long moduleId = 1L;
        Long moduleResourceId = 1L;

        ModuleResource existing = moduleResource;

        UpdateModuleResourceRequest request = ModuleResourceTestFactory.updateModuleResourceRequest(
                moduleId, "Updated test"
        );

        ModuleResourceResponse response = new ModuleResourceResponse(
                1L,
                1L,
                "Updated test",
                ModuleResourceType.MP3,
                "https://updatedUrl",
                "https://updatedThumbnailUrl",
                1
        );

        Mockito.when(repository.findByModuleIdAndId(moduleId, moduleResourceId))
                .thenReturn(Optional.of(existing));
        Mockito.when(mapper.toResponse(existing))
                .thenReturn(response);

        ModuleResourceResponse updated = service.updateModuleResource(
                moduleId, moduleResourceId, request
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Updated test", updated.name());
        Assertions.assertEquals(ModuleResourceType.MP3, updated.type());

        Mockito.verify(repository).findByModuleIdAndId(moduleId, moduleResourceId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(existing);
    }

    @Test
    @DisplayName("Func getModuleResourceById should return ModuleResource and response")
    void getModuleResourceById_shouldReturnModuleResource_andReturnResponse() {
        Long moduleId = 1L;
        Long moduleResourceId = 1L;

        ModuleResource existing = moduleResource;

        ModuleResourceResponse response = new ModuleResourceResponse(
                1L,
                1L,
                "Test",
                ModuleResourceType.MP4,
                "https://url",
                "https://thumbnailUrl",
                1
        );

        Mockito.when(repository.findByModuleIdAndId(moduleId, moduleResourceId)).thenReturn(Optional.of(existing));
        Mockito.when(mapper.toResponse(existing)).thenReturn(response);

        ModuleResourceResponse gotten = service.getModuleResourceById(moduleId, moduleResourceId);

        Assertions.assertNotNull(gotten);
        Assertions.assertEquals(response, gotten);

        Mockito.verify(repository, Mockito.times(1)).findByModuleIdAndId(moduleId, moduleResourceId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(existing);
    }

    @Test
    @DisplayName("Func deleteModuleResource should delete ModuleResource")
    void deleteModuleResource_shouldDeleteModuleResource() {
        Long moduleId = 1L;
        Long moduleResourceId = 1L;

        ModuleResource existing = moduleResource;

        Mockito.when(repository.findByModuleIdAndId(moduleId, moduleResourceId))
                .thenReturn(Optional.of(existing));

        service.deleteModuleResource(moduleId, moduleResourceId);

        Mockito.verify(repository).findByModuleIdAndId(moduleId, moduleResourceId);
        Mockito.verify(repository).delete(existing);
    }

    @Test
    @DisplayName("Func getAllModuleResources should return list of ModuleResource's")
    void getAllModuleResources_shouldReturnList_ofModuleResource() {
        Long moduleId = 1L;

        ModuleResource r1 = new ModuleResource();
        ModuleResource r2 = new ModuleResource();

        ModuleResourceResponse resp1 = new ModuleResourceResponse(
                1L,
                1L,
                "Test 1",
                ModuleResourceType.MP4,
                "https://url1",
                "https://thumbnailUrl1",
                1
        );
        ModuleResourceResponse resp2 = new ModuleResourceResponse(
                2L,
                1L,
                "Test 2",
                ModuleResourceType.MP3,
                "https://url2",
                "https://thumbnailUrl2",
                2
        );

        Mockito.when(repository.findAllByModuleIdOrderByPositionAsc(moduleId))
                .thenReturn(List.of(r1, r2));

        Mockito.when(mapper.toResponse(r1)).thenReturn(resp1);
        Mockito.when(mapper.toResponse(r2)).thenReturn(resp2);

        List<ModuleResourceResponse> result = service.getAllModuleResources(moduleId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(resp1, result.get(0));
        Assertions.assertEquals(resp2, result.get(1));

        Mockito.verify(repository).findAllByModuleIdOrderByPositionAsc(moduleId);
        Mockito.verify(mapper).toResponse(r1);
        Mockito.verify(mapper).toResponse(r2);
    }

}
