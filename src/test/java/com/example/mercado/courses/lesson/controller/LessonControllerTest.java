package com.example.mercado.courses.lesson.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;
import com.example.mercado.courses.lesson.enums.LessonStatus;
import com.example.mercado.courses.lesson.service.interfaces.LessonService;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("LessonController Test")
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LessonService service;

    @Test
    @DisplayName("Endpoint createLesson should return response")
    void createLesson_shouldReturnResponse() throws Exception {
        Long moduleId = 1L;

        CreateLessonRequest request = new CreateLessonRequest(
                "Test",
                "Test description",
                10,
                LessonStatus.PUBLISHED
        );

        LessonResponse response = new LessonResponse(
                1L,
                moduleId,
                "Test",
                "Test description",
                LessonStatus.PUBLISHED,
                10,
                1
        );

        Mockito.when(service.createLesson(moduleId, request)).thenReturn(response);

        mockMvc.perform(post("/modules/{moduleId}/lessons", moduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value(LessonStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.duration").value(10))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).createLesson(moduleId, request);
    }

    @Test
    @DisplayName("Endpoint updateLesson should return response")
    void updateLesson_shouldReturnResponse() throws Exception {
        Long moduleId = 1L;
        Long lessonId = 1L;

        UpdateLessonRequest request = new UpdateLessonRequest(
                "Updated test",
                "Updated description",
                20,
                LessonStatus.ARCHIVED
        );

        LessonResponse response = new LessonResponse(
                1L,
                moduleId,
                "Updated test",
                "Updated description",
                LessonStatus.ARCHIVED,
                20,
                1
        );

        Mockito.when(service.updateLesson(moduleId, lessonId, request)).thenReturn(response);

        mockMvc.perform(patch("/modules/{moduleId}/lessons/{lessonId}", moduleId, lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated test"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.status").value(LessonStatus.ARCHIVED.name()))
                .andExpect(jsonPath("$.duration").value(20))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updateLesson(moduleId, lessonId, request);
    }

    @Test
    @DisplayName("Endpoint getLesson should return response")
    void getLesson_shouldReturnResponse() throws Exception {
        Long moduleId = 1L;
        Long lessonId = 1L;

        LessonResponse response = new LessonResponse(
                1L,
                moduleId,
                "Test",
                "Test description",
                LessonStatus.PUBLISHED,
                10,
                1
        );

        Mockito.when(service.getLesson(moduleId, lessonId)).thenReturn(response);

        mockMvc.perform(get("/modules/{moduleId}/lessons/{lessonId}", moduleId, lessonId))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.moduleId").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value(LessonStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.duration").value(10))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).getLesson(moduleId, lessonId);
    }

    @Test
    @DisplayName("Endpoint deleteLesson should delete lesson from database")
    void deleteLesson_shouldDeleteLesson_fromDatabase() throws Exception {
        Long moduleId = 1L;
        Long lessonId = 1L;

        mockMvc.perform(delete("/modules/{moduleId}/lessons/{lessonId}", moduleId, lessonId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteLesson(moduleId, lessonId);
    }

    @Test
    @DisplayName("Endpoint getAllLessons should return list of response")
    void getAllLessons_shouldReturnList_ofResponse() throws Exception {
        Long moduleId = 1L;

        List<LessonResponse> lessons = List.of(
                new LessonResponse(
                        1L,
                        moduleId,
                        "Test 1",
                        "Desc 1",
                        LessonStatus.PUBLISHED,
                        10,
                        1),
                new LessonResponse(
                        2L, moduleId,
                        "Test 2",
                        "Desc 2",
                        LessonStatus.ARCHIVED,
                        20,
                        2
                )
        );

        Mockito.when(service.getLessonsByModuleId(moduleId)).thenReturn(lessons);

        mockMvc.perform(get("/modules/{moduleId}/lessons", moduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test 1"))
                .andExpect(jsonPath("$[1].name").value("Test 2"));

        Mockito.verify(service).getLessonsByModuleId(moduleId);
    }

}
