package com.example.mercado.courses.moduleResource.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.courses.moduleResource.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleResource.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;
import com.example.mercado.courses.moduleResource.service.interfaces.ModuleResourceService;
import com.example.mercado.courses.testutils.moduleResource.ModuleResourceTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModuleResourceController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("ModuleResourceController Test")
public class ModuleResourceControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModuleResourceService service;

    @Test
    @DisplayName("Endpoint createModuleResource should return response and create new ModuleResource")
    void createModuleResource_shouldReturnResponse() throws Exception {
        Long moduleId = 1L;

        CreateModuleResourceRequest request = ModuleResourceTestFactory.createModuleResourceRequest();

        ModuleResourceResponse response = new ModuleResourceResponse(
                1L,
                moduleId,
                "Test",
                ModuleResourceType.MP4,
                "https://url",
                "https://thumbnailUrl",
                1
        );

        Mockito.when(service.createModuleResource(moduleId, request))
                .thenReturn(response);

        mockMvc.perform(post("/modules/{moduleId}/resources", moduleId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.moduleId").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.type").value(ModuleResourceType.MP4.name()))
                .andExpect(jsonPath("$.url").value("https://url"))
                .andExpect(jsonPath("$.thumbnailUrl").value("https://thumbnailUrl"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).createModuleResource(moduleId, request);
    }

    @Test
    @DisplayName("Endpoint updateModuleResource should return response and update parameter's of ModuleResource")
    void updateModuleResource_shouldReturnResponse() throws Exception {
        Long moduleId = 1L;
        Long resourceId = 1L;

        UpdateModuleResourceRequest request = ModuleResourceTestFactory.updateModuleResourceRequest(
                moduleId, "Updated Test"
        );

        ModuleResourceResponse response = new ModuleResourceResponse(
                1L,
                moduleId,
                "Updated Test",
                ModuleResourceType.MP3,
                "https://updatedUrl",
                "https://updatedThumbnailUrl",
                1
        );

        Mockito.when(service.updateModuleResource(moduleId, resourceId, request)).thenReturn(response);

        mockMvc.perform(patch("/modules/{moduleId}/resources/{resourceId}", moduleId, resourceId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.moduleId").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Test"))
                .andExpect(jsonPath("$.type").value(ModuleResourceType.MP3.name()))
                .andExpect(jsonPath("$.url").value("https://updatedUrl"))
                .andExpect(jsonPath("$.thumbnailUrl").value("https://updatedThumbnailUrl"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updateModuleResource(moduleId, resourceId, request);
    }

    @Test
    @DisplayName("Endpoint getModuleResourceById should return response and ModuleResource")
    void getModuleResourceById_shouldReturnResponse() throws Exception {
        Long moduleId = 1L;
        Long resourceId = 1L;

        ModuleResourceResponse response = new ModuleResourceResponse(
                1L,
                moduleId,
                "Test",
                ModuleResourceType.MP4,
                "https://url",
                "https://thumbnailUrl",
                1
        );

        Mockito.when(service.getModuleResourceById(moduleId, resourceId)).thenReturn(response);

        mockMvc.perform(get("/modules/{moduleId}/resources/{resourceId}", moduleId, resourceId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.moduleId").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.type").value(ModuleResourceType.MP4.name()))
                .andExpect(jsonPath("$.url").value("https://url"))
                .andExpect(jsonPath("$.thumbnailUrl").value("https://thumbnailUrl"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).getModuleResourceById(moduleId, resourceId);
    }

    @Test
    @DisplayName("Endpoint deleteModuleResource should delete ModuleResource")
    void deleteModuleResource_shouldDeleteModuleResource() throws Exception {
        Long moduleId = 1L;
        Long resourceId = 1L;

        Mockito.doNothing()
                .when(service)
                .deleteModuleResource(moduleId, resourceId);

        mockMvc.perform(delete("/modules/{moduleId}/resources/{resourceId}", moduleId, resourceId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteModuleResource(moduleId, resourceId);
    }

    @Test
    @DisplayName("Endpoint getAllModuleResources should return list of all ModuleResource's")
    void getAllModuleResources_shouldReturnList_ofAllModuleResources() throws Exception {
        Long moduleId = 1L;

        List<ModuleResourceResponse> responseList = List.of(
                new ModuleResourceResponse(
                        1L,
                        moduleId,
                        "Test1",
                        ModuleResourceType.MP4,
                        "https://url1",
                        "https://thumbnailUrl1",
                        1
                ),
                new ModuleResourceResponse(
                        2L,
                        moduleId,
                        "Test2",
                        ModuleResourceType.MP3,
                        "https://url2",
                        "https://thumbnailUrl2",
                        2
                )
        );

        Mockito.when(service.getAllModuleResources(moduleId))
                .thenReturn(responseList);

        mockMvc.perform(get("/modules/{moduleId}/resources", moduleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(2))

                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test1"))
                .andExpect(jsonPath("$[0].type").value(ModuleResourceType.MP4.name()))
                .andExpect(jsonPath("$[0].url").value("https://url1"))
                .andExpect(jsonPath("$[0].thumbnailUrl").value("https://thumbnailUrl1"))

                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Test2"))
                .andExpect(jsonPath("$[1].type").value(ModuleResourceType.MP3.name()))
                .andExpect(jsonPath("$[1].url").value("https://url2"))
                .andExpect(jsonPath("$[1].thumbnailUrl").value("https://thumbnailUrl2"));

        Mockito.verify(service, Mockito.times(1)).getAllModuleResources(moduleId);
    }

}
