package com.example.mercado.unit.courses.module.controller.publicController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.module.controller.publicController.ModulePublicController;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.service.publicService.ModulePublicService;
import com.example.mercado.testUtils.courses.module.ModuleTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModulePublicController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("Module-Public Controller Test")
public class ModulePublicControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ModulePublicService service;

        // =========================
        // getModuleById
        // =========================
        @Nested
        class GetModuleById {

                @Test
                void getModuleById_shouldReturnModuleResponse_whenValidRequest() throws Exception {
                        Long courseId = 1L;
                        Long moduleId = 2L;

                        ModuleResponse response = ModuleTestData.moduleResponse(m -> {
                                m.id = moduleId;
                                m.courseId = courseId;
                        });

                        when(service.getModuleById(courseId, moduleId))
                                .thenReturn(response);

                        mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(moduleId))
                                .andExpect(jsonPath("$.name").value("Test module"))
                                .andExpect(jsonPath("$.courseId").value(courseId));

                        verify(service, times(1)).getModuleById(courseId, moduleId);
                }

                @Test
                void getModuleById_shouldThrowException_whenModuleNotFound() throws Exception {
                        Long courseId = 1L;
                        Long moduleId = 2L;

                        when(service.getModuleById(courseId, moduleId))
                                .thenThrow(new AppException(ErrorCode.MODULE_NOT_FOUND));

                        mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                                .andExpect(status().isNotFound());
                }

                @ParameterizedTest
                @MethodSource("badIdInRequest")
                void getModuleById_shouldThrowException_whenIdInRequestIsInvalid(String url) throws Exception {
                        mockMvc.perform(get(url))
                                .andExpect(status().isBadRequest());

                        verify(service, never()).getModuleById(anyLong(), anyLong());
                }

                private static Stream<String> badIdInRequest() {
                        return Stream.of(
                                "/api/courses/-1/modules/1",
                                "/api/courses/1/modules/-1",
                                "/api/courses/-1/modules/-1",
                                "/api/courses/a/modules/1",
                                "/api/courses/1/modules/a"
                        );
                }



        }

        // =========================
        // getAllModules
        // =========================
        @Nested
        class GetAllModules {

                @Test
                void getAllModulesByCourseId_shouldReturnCorrectPages_whenModulesExistsAndValidRequest() throws Exception {
                        Long courseId = 1L;

                        List<ModuleShortResponse> responses = List.of(
                                ModuleTestData.moduleShortResponse(module -> module.id = 1L),
                                ModuleTestData.moduleShortResponse(module -> module.id = 2L)
                        );

                        Page<ModuleShortResponse> page = new PageImpl<>(responses);

                        when(service.getAllModulesByCourseId(eq(courseId), any(Pageable.class)))
                                .thenReturn(page);

                        mockMvc.perform(get("/api/courses/{courseId}/modules", courseId)
                                        .param("page", "0")
                                        .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content.length()").value(2))
                                .andExpect(jsonPath("$.content[0].id").value(1L))
                                .andExpect(jsonPath("$.content[1].id").value(2L));

                        verify(service).getAllModulesByCourseId(eq(courseId), any(Pageable.class));
                }

                @Test
                void getAllModulesByCourseId_shouldReturnEmptyPage_whenNoOneModuleExists() throws Exception {
                        Long courseId = 1L;

                        Page<ModuleShortResponse> page = new PageImpl<>(Collections.emptyList());

                        when(service.getAllModulesByCourseId(eq(courseId), any(Pageable.class)))
                                .thenReturn(page);

                        mockMvc.perform(get("/api/courses/{courseId}/modules", courseId)
                                        .param("page", "0")
                                        .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content").isEmpty())
                                .andExpect(jsonPath("$.totalElements").value(0));

                        verify(service).getAllModulesByCourseId(eq(courseId), any(Pageable.class));
                }

                @ParameterizedTest
                @MethodSource("badIdInRequest")
                void getAllModulesByCourseId_shouldThrowException_whenIdInRequestIsInvalid(String url) throws Exception {
                        mockMvc.perform(get(url))
                                .andExpect(status().isBadRequest());

                        verify(service, never()).getAllModulesByCourseId(anyLong(), any(Pageable.class));
                }

                private static Stream<String> badIdInRequest() {
                        return Stream.of(
                                "/api/courses/-1/modules",
                                "/api/courses/-100/modules",
                                "/api/courses/a/modules"
                        );
                }

        }

        // =========================
        // countAllModules
        // =========================
        @Nested
        class CountAllModules {

                @Test
                void countAllModules_shouldReturnCorrectLongNumberOfModules_whenExistsAndValidRequest() throws Exception {
                        Long courseId = 1L;

                        when(service.countAllModules(courseId))
                                .thenReturn(5L);

                        mockMvc.perform(get("/api/courses/{courseId}/modules/count", courseId))
                                .andExpect(status().isOk())
                                .andExpect(content().string("5"));

                        verify(service).countAllModules(courseId);
                }

                @Test
                void countAllModules_shouldReturnZero_whenNoOneModuleExists() throws Exception {
                        Long courseId = 1L;

                        when(service.countAllModules(courseId))
                                .thenReturn(0L);

                        mockMvc.perform(get("/api/courses/{courseId}/modules/count", courseId))
                                .andExpect(status().isOk())
                                .andExpect(content().string("0"));

                        verify(service).countAllModules(courseId);
                }

                @ParameterizedTest
                @MethodSource("badIdInRequest")
                void countAllModules_shouldThrowException_whenIdInRequestIsInvalid(String url) throws Exception {
                        mockMvc.perform(get(url))
                                .andExpect(status().isBadRequest());

                        verify(service, never()).countAllModules(anyLong());
                }

                private static Stream<String> badIdInRequest() {
                        return Stream.of(
                                "/api/courses/-1/modules/count",
                                "/api/courses/-100/modules/count",
                                "/api/courses/a/modules/count"
                        );
                }

        }

}
