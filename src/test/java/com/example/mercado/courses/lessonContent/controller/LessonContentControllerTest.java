package com.example.mercado.courses.lessonContent.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;
import com.example.mercado.courses.lessonContent.enums.LessonContentType;
import com.example.mercado.courses.lessonContent.service.interfaces.LessonContentService;
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

@WebMvcTest(LessonContentController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
public class LessonContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LessonContentService service;


    @Test
    @DisplayName("Endpoint createLessonContent should return response")
    void createLessonContent_shouldReturnResponse() throws Exception {
        Long lessonId = 1L;

        CreateLessonContentRequest request = new CreateLessonContentRequest(
                "test",
                "content",
                LessonContentType.MP4,
                "url",
                "thumbnailUrl"
        );

        LessonContentResponse response = new LessonContentResponse(
                1L,
                lessonId,
                "test",
                "content",
                1,
                "url",
                "thumbnailUrl",
                LessonContentType.MP4
        );

        Mockito.when(service.createLessonContent(Mockito.eq(lessonId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/lessons/{lessonId}/contents", lessonId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("content"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service).createLessonContent(Mockito.eq(lessonId), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint updateLessonContent should return response")
    void updateLessonContent_shouldReturnResponse() throws Exception {
        Long lessonId = 1L;
        Long contentId = 1L;

        UpdateLessonContentRequest request = new UpdateLessonContentRequest(
                "Updated test",
                "Updated content",
                LessonContentType.MP3,
                "Updated url",
                "Updated thumbnailUrl"
        );

        LessonContentResponse response = new LessonContentResponse(
                1L,
                lessonId,
                "Updated test",
                "Updated content",
                1,
                "Updated url",
                "Updated thumbnailUrl",
                LessonContentType.MP3
        );

        Mockito.when(service.updateLessonContent(Mockito.eq(lessonId), Mockito.eq(contentId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/lessons/{lessonId}/contents/{contentId}", lessonId, contentId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated test"))
                .andExpect(jsonPath("$.description").value("Updated content"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service).updateLessonContent(Mockito.eq(lessonId), Mockito.eq(contentId), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getLessonContentById should return response")
    void getLessonContentById_shouldReturnResponse() throws Exception {
        Long lessonId = 1L;
        Long contentId = 1L;

        LessonContentResponse response = new LessonContentResponse(
                1L,
                lessonId,
                "test",
                "content",
                1,
                "url",
                "thumbnailUrl",
                LessonContentType.MP4
        );

        Mockito.when(service.getLessonContentById(Mockito.eq(lessonId), Mockito.eq(contentId)))
                .thenReturn(response);

        mockMvc.perform(get("/lessons/{lessonId}/contents/{contentId}", lessonId, contentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("content"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service).getLessonContentById(Mockito.eq(lessonId), Mockito.eq(contentId));
    }

    @Test
    @DisplayName("Endpoint deleteLessonContent should delete LessonContent from database")
    void deleteLessonContent_shouldDeleteLessonContent_fromDataBase() throws Exception {
        Long lessonId = 1L;
        Long contentId = 1L;

        Mockito.doNothing()
                .when(service)
                .deleteLessonContent(Mockito.eq(lessonId), Mockito.eq(contentId));

        mockMvc.perform(delete("/lessons/{lessonId}/contents/{contentId}", lessonId, contentId))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deleteLessonContent(lessonId, contentId);
    }

    @Test
    @DisplayName("Endpoint getAllLessonContents should return list of lesson content response")
    void getAllLessonContents_shouldReturnListOf_LessonContentResponse() throws Exception {
        Long lessonId = 1L;

        List<LessonContentResponse> responseList = List.of(
                new LessonContentResponse(
                        1L, lessonId, "title1", "content1", 1,
                        "url1", "thumb1", LessonContentType.MP4
                ),
                new LessonContentResponse(
                        2L, lessonId, "title2", "content2", 2,
                        "url2", "thumb2", LessonContentType.PDF
                )
        );

        Mockito.when(service.getAllLessonContents(lessonId))
                .thenReturn(responseList);

        mockMvc.perform(get("/lessons/{lessonId}/contents", lessonId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(2))

                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("title1"))
                .andExpect(jsonPath("$[0].type").value("MP4"))

                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("title2"))
                .andExpect(jsonPath("$[1].type").value("PDF"));

        Mockito.verify(service).getAllLessonContents(lessonId);
    }
}
