package com.example.mercado.unit.courses.module.controller.adminController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.module.controller.adminController.ModuleAdminController;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ReorderModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.service.adminService.ModuleAdminService;
import com.example.mercado.testUtils.courses.module.ModuleTestData;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModuleAdminController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("Module-Admin Controller Test")
public class ModuleAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModuleAdminService service;
    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // createModule
    // =========================
    @Nested
    class CreateModule {

        @Test
        void createModule_shouldCreateModuleAndReturnResponseAndStatusCreated_whenValidRequest() throws Exception {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });
            ModuleResponse response = ModuleTestData.moduleResponse(r -> {
                r.name = "New name";
                r.description = "New description";
                r.courseId = courseId;
            });

            when(service.createModule(courseId, request))
                    .thenReturn(response);

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("New name"))
                    .andExpect(jsonPath("$.description").value("New description"))
                    .andExpect(jsonPath("$.courseId").value(1L));

            Assertions.assertAll(
                    () -> verify(service, times(1)).createModule(courseId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void createModule_shouldThrowException_whenIdIsInvalid(String courseId) throws Exception {
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verifyNoMoreInteractions(service)
            );
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "-1", "a", "#", "A"
            );
        }

        @Test
        void createModule_shouldThrowException_whenCourseAlreadyHasModuleWithASameName() throws Exception {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });

            when(service.createModule(courseId, request))
                    .thenThrow(
                            new AppException(ErrorCode.MODULE_ALREADY_EXISTS)
                    );

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());

            Assertions.assertAll(
                    () -> verify(service, times(1)).createModule(courseId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @ValueSource(strings = {
                " ", "    ", "!", "@",
                "#", "$", "%", "^", "&"
        })
        void createModule_shouldThrowException_whenRequestIsInvalid(String name) throws Exception {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(r -> {
                r.name = name;
                r.description = "New description";
            });

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).createModule(courseId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

    }

    // =========================
    // updateModule
    // =========================
    @Nested
    class UpdateModule {

        @Test
        void updateModule_shouldUpdateModuleAndReturnResponse_whenValidRequest() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });
            ModuleResponse response = ModuleTestData.moduleResponse(r -> {
                r.name = "New name";
                r.description = "New description";
                r.courseId = 1L;
                r.id = 2L;
            });

            when(service.updateModule(courseId, moduleId, request))
                    .thenReturn(response);

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New name"))
                    .andExpect(jsonPath("$.description").value("New description"))
                    .andExpect(jsonPath("$.courseId").value(1L))
                    .andExpect(jsonPath("$.id").value(2L));

            Assertions.assertAll(
                    () -> verify(service, times(1)).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @Test
        void updateModule_shouldUpdateOnlyName_whenDescriptionInRequestIsNull() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = "New name";
                r.description = null;
            });
            ModuleResponse response = ModuleTestData.moduleResponse(r -> {
                r.name = "New name";
                r.description = "Old description";
                r.courseId = 1L;
                r.id = 2L;
            });

            when(service.updateModule(courseId, moduleId, request))
                    .thenReturn(response);

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New name"))
                    .andExpect(jsonPath("$.description").value("Old description"))
                    .andExpect(jsonPath("$.courseId").value(1L))
                    .andExpect(jsonPath("$.id").value(2L));

            Assertions.assertAll(
                    () -> verify(service, times(1)).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @Test
        void updateModule_shouldUpdateOnlyDescription_whenNameInRequestIsNull() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = null;
                r.description = "New description";
            });
            ModuleResponse response = ModuleTestData.moduleResponse(r -> {
                r.name = "Old name";
                r.description = "New description";
                r.courseId = 1L;
                r.id = 2L;
            });

            when(service.updateModule(courseId, moduleId, request))
                    .thenReturn(response);

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Old name"))
                    .andExpect(jsonPath("$.description").value("New description"))
                    .andExpect(jsonPath("$.courseId").value(1L))
                    .andExpect(jsonPath("$.id").value(2L));

            Assertions.assertAll(
                    () -> verify(service, times(1)).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @Test
        void updateModule_shouldThrowException_whenModuleNotFound() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });

            when(service.updateModule(courseId, moduleId, request))
                    .thenThrow(
                            new AppException(ErrorCode.MODULE_NOT_FOUND)
                    );

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            Assertions.assertAll(
                    () -> verify(service, times(1)).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @Test
        void updateModule_shouldThrowException_whenCourseAlreadyHasModuleWithASameName() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });

            when(service.updateModule(courseId, moduleId, request))
                    .thenThrow(
                            new AppException(ErrorCode.MODULE_ALREADY_EXISTS)
                    );

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());

            Assertions.assertAll(
                    () -> verify(service, times(1)).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void updateModule_shouldThrowException_whenIdInRequestIsInvalid(String url) throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = "New name";
                r.description = "New description";
            });

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules/1",
                    "/api/admin/courses/1/modules/-1",
                    "/api/admin/courses/-1/modules/-1",
                    "/api/admin/courses/a/modules/1",
                    "/api/admin/courses/1/modules/a",
                    "/api/admin/courses/A/modules/A",
                    "/api/admin/courses/!/modules/1",
                    "/api/admin/courses/1/modules/!"
            );
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = {
                " ", "    ", "!", "@",
                "#", "$", "%", "^", "&"
        })
        void updateModule_shouldThrowException_whenRequestIsInvalid(String name) throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(r -> {
                r.name = name;
                r.description = "New description";
            });

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).updateModule(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

    }

    // =========================
    // deleteModule
    // =========================
    @Nested
    class DeleteModule {

        @Test
        void deleteModule_shouldDeleteModuleAndReturnStatusNoContent_whenValidRequest() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                    .andExpect(status().isNoContent());

            Assertions.assertAll(
                    () -> verify(service, times(1)).deleteModule(courseId, moduleId),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @Test
        void deleteModule_shouldThrowException_whenModuleNotFound() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            doThrow(new AppException(ErrorCode.MODULE_NOT_FOUND))
                    .when(service)
                    .deleteModule(courseId, moduleId);

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                    .andExpect(status().isNotFound());

            Assertions.assertAll(
                    () -> verify(service, times(1)).deleteModule(courseId, moduleId),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void deleteModule_shouldThrowException_whenIdInRequestIsInvalid(String url) throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            mockMvc.perform(delete(url))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).deleteModule(courseId, moduleId),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules/1",
                    "/api/admin/courses/1/modules/-1",
                    "/api/admin/courses/-1/modules/-1",
                    "/api/admin/courses/a/modules/1",
                    "/api/admin/courses/1/modules/a",
                    "/api/admin/courses/A/modules/A",
                    "/api/admin/courses/!/modules/1",
                    "/api/admin/courses/1/modules/!"
            );
        }

    }

    // =========================
    // deleteAllModules
    // =========================
    @Nested
    class DeleteAllModules {

        @Test
        void deleteAllModules_shouldDeleteAllModulesFromCourseAndReturnStatusNoContent_whenValidRequest() throws Exception {
            Long courseId = 1L;

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules", courseId))
                    .andExpect(status().isNoContent());

            Assertions.assertAll(
                    () -> verify(service, times(1)).deleteAllModulesByCourseId(courseId),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void deleteAllModules_shouldThrowException_whenIdIsInvalid(String url) throws Exception {
            Long courseId = 1L;

            mockMvc.perform(delete(url))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).deleteAllModulesByCourseId(courseId),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules",
                    "/api/admin/courses/a/modules",
                    "/api/admin/courses/A/modules",
                    "/api/admin/courses/!/modules"
            );
        }

    }

    // =========================
    // updatePosition
    // =========================
    @Nested
    class UpdatePosition {

        @Test
        void updatePosition_shouldUpdateModulePosition_whenValidRequest() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            ModuleResponse response = ModuleTestData.moduleResponse(m -> {
                m.name = "Module";
                m.courseId = courseId;
                m.position = 2;
            });

            when(service.updatePosition(courseId, moduleId, request))
                    .thenReturn(response);

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, moduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Module"))
                    .andExpect(jsonPath("$.position").value(2))
                    .andExpect(jsonPath("$.courseId").value(1L));

            Assertions.assertAll(
                    () -> verify(service, times(1)).updatePosition(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @Test
        void updatePosition_shouldThrowException_whenModuleNotFound() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            ModuleResponse response = ModuleTestData.moduleResponse(m -> {
                m.name = "Module";
                m.courseId = courseId;
                m.position = 2;
            });

            when(service.updatePosition(courseId, moduleId, request))
                    .thenThrow(
                            new AppException(ErrorCode.MODULE_NOT_FOUND)
                    );

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            Assertions.assertAll(
                    () -> verify(service, times(1)).updatePosition(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {
                -1
        })
        void updatePosition_shouldThrowException_whenRequestIsInvalid(Integer position) throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            ReorderModuleRequest request = new ReorderModuleRequest(position);

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, moduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).updatePosition(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void updatePosition_shouldThrowException_whenIdInRequestIsInvalid(String url) throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            Assertions.assertAll(
                    () -> verify(service, never()).updatePosition(courseId, moduleId, request),
                    () -> verifyNoMoreInteractions(service)
            );
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules/1/position",
                    "/api/admin/courses/1/modules/-1/position",
                    "/api/admin/courses/a/modules/1/position",
                    "/api/admin/courses/1/modules/a/position",
                    "/api/admin/courses/!/modules/1/position",
                    "/api/admin/courses/1/modules/!/position"
            );
        }

    }

}
