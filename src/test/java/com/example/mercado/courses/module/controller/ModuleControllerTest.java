package com.example.mercado.courses.module.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import com.example.mercado.courses.module.service.interfaces.ModuleService;
import com.example.mercado.courses.testutils.module.ModuleTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModuleController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("Module Controller Test")
public class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModuleService service;


    @Test
    @DisplayName("Endpoint createModule should return response")
    void createModule_shouldReturnResponse() throws Exception {
        Long courseId = 1L;

        CreateModuleRequest request = ModuleTestFactory.createModuleRequest();

        ModuleResponse response = new ModuleResponse(
                1L,
                1L,
                "Test module",
                "Test description",
                ModuleStatus.PUBLISHED,
                ModuleType.FREE,
                null,
                null
        );

        Mockito.when(service.createModule(courseId, request))
                .thenReturn(response);

        mockMvc.perform(post("/courses/{courseId}/modules", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test module"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value(ModuleStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(ModuleType.FREE.name()));
        Mockito.verify(service, Mockito.times(1)).createModule(courseId, request);
    }

    @Test
    @DisplayName("Endpoint updateModule should return response")
    void updateModule_shouldReturnResponse() throws Exception {
        Long courseId = 1L;
        Long moduleId = 1L;

        UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest();

        ModuleResponse response = new ModuleResponse(
                1L,
                1L,
                "Updated module",
                "Updated description",
                ModuleStatus.PUBLISHED,
                ModuleType.FREE,
                null,
                null
        );

        Mockito.when(service.updateModule(courseId, moduleId, request))
                .thenReturn(response);

        mockMvc.perform(patch("/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.name").value("Updated module"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.status").value(ModuleStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(ModuleType.FREE.name()));

        Mockito.verify(service, Mockito.times(1)).updateModule(courseId, moduleId, request);
    }

    @Test
    @DisplayName("Endpoint getModuleById should return response")
    void getModuleById_shouldReturnResponse() throws Exception {
        Long courseId = 1L;
        Long moduleId = 1L;

        ModuleResponse response = new ModuleResponse(
                1L,
                1L,
                "Test module",
                "Test description",
                ModuleStatus.PUBLISHED,
                ModuleType.FREE,
                null,
                null
        );

        Mockito.when(service.getModuleById(courseId, moduleId))
                .thenReturn(response);

        mockMvc.perform(get("/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.name").value("Test module"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value(ModuleStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(ModuleType.FREE.name()));

        Mockito.verify(service, Mockito.times(1)).getModuleById(courseId, moduleId);
    }

    @Test
    @DisplayName("Endpoint deleteModule should delete module from DB")
    void deleteModule_shouldDeleteModule() throws Exception {
        Long courseId = 1L;
        Long moduleId = 1L;

        mockMvc.perform(delete("/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteModule(courseId, moduleId);
    }

    @Test
    @DisplayName("Endpoint publishModule should return response")
    void publishModule_shouldReturnResponse() throws Exception {
        Long courseId = 1L;
        Long moduleId = 1L;

        ModuleResponse response = new ModuleResponse(
                1L,
                1L,
                "Test module",
                "Test description",
                ModuleStatus.PUBLISHED,
                ModuleType.FREE,
                null,
                null
        );

        Mockito.when(service.publishModule(courseId, moduleId))
                .thenReturn(response);

        mockMvc.perform(patch("/courses/{courseId}/modules/{moduleId}/publish", courseId, moduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.name").value("Test module"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value(ModuleStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(ModuleType.FREE.name()));

        Mockito.verify(service, Mockito.times(1)).publishModule(courseId, moduleId);
    }

    @Test
    @DisplayName("Endpoint archiveModule should return response")
    void archiveModule_shouldReturnResponse() throws Exception {
        Long courseId = 1L;
        Long moduleId = 1L;

        ModuleResponse response = new ModuleResponse(
                1L,
                1L,
                "Test module",
                "Test description",
                ModuleStatus.ARCHIVED,
                ModuleType.FREE,
                null,
                null
        );

        Mockito.when(service.archiveModule(courseId, moduleId))
                .thenReturn(response);

        mockMvc.perform(patch("/courses/{courseId}/modules/{moduleId}/archive", courseId, moduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.name").value("Test module"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value(ModuleStatus.ARCHIVED.name()))
                .andExpect(jsonPath("$.type").value(ModuleType.FREE.name()));

        Mockito.verify(service, Mockito.times(1)).archiveModule(courseId, moduleId);
    }

    @Test
    @DisplayName("Endpoint moveModuleUp should move module upper")
    void moveModuleUp_shouldMoveModuleUpper() throws Exception {
        Long courseId = 1L;
        Long moduleId = 2L;

        mockMvc.perform(patch("/courses/{courseId}/modules/{moduleId}/move-up", courseId, moduleId))
                .andExpect(status().isOk());

        Mockito.verify(service).moveModuleUp(courseId, moduleId);
    }

    @Test
    @DisplayName("Endpoint moveModuleDown should move module lower")
    void moveModuleDown_shouldMoveModuleLower() throws Exception {
        Long courseId = 1L;
        Long moduleId = 2L;

        mockMvc.perform(patch("/courses/{courseId}/modules/{moduleId}/move-down", courseId, moduleId))
                .andExpect(status().isOk());

        Mockito.verify(service).moveModuleDown(courseId, moduleId);
    }

    @Test
    @DisplayName("Endpoint getCourseModules should return page of ModuleShortResponse")
    void getCourseModules_shouldReturnPage_ofModuleShortResponse() throws Exception {
        Long courseId = 1L;

        ModuleShortResponse r1 = new ModuleShortResponse(
                1L,
                "Java",
                ModuleStatus.PUBLISHED
        );
        ModuleShortResponse r2 = new ModuleShortResponse(
                2L,
                "Python",
                ModuleStatus.ARCHIVED
        );

        Page<ModuleShortResponse> page = new PageImpl<>(
                List.of(r1, r2),
                PageRequest.of(0, 10),
                2
        );

        Mockito.when(service.getCourseModules(Mockito.eq(courseId), Mockito.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/courses/{courseId}/modules", courseId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value(r1.name()))
                .andExpect(jsonPath("$.content[1].name").value(r2.name()))
                .andExpect(jsonPath("$.totalElements").value(2));

        Mockito.verify(service)
                .getCourseModules(Mockito.eq(courseId), Mockito.any(Pageable.class));
    }

}
