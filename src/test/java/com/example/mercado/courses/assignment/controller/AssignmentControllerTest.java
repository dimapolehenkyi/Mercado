package com.example.mercado.courses.assignment.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;
import com.example.mercado.courses.assignment.entity.Assignment;
import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;
import com.example.mercado.courses.assignment.service.interfaces.AssignmentService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("AssignmentController Test")
public class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssignmentService service;


    @Test
    @DisplayName("Endpoint createAssignment should return response")
    void createAssignment_shouldReturnResponse() throws Exception {
        Long lessonId = 1L;

        CreateAssignmentRequest request = new CreateAssignmentRequest(
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100
        );

        AssignmentResponse testResponse = new AssignmentResponse(
                1L,
                1L,
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        Mockito.when(service.createAssignment(lessonId, request)).thenReturn(testResponse);

        mockMvc.perform(post("/lessons/{lessonId}/assignments", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lessonId").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.type").value(AssignmentType.TEST.name()))
                .andExpect(jsonPath("$.status").value(AssignmentStatus.ASSIGNED.name()))
                .andExpect(jsonPath("$.maxScore").value(100))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).createAssignment(lessonId, request);
    }

    @Test
    @DisplayName("Endpoint updateAssignment should return response")
    void updateAssignment_shouldReturnResponse() throws Exception {
        Long lessonId = 1L;
        Long assignmentId = 1L;

        UpdateAssignmentRequest request = new UpdateAssignmentRequest(
                "Updated test",
                "Updated test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100
        );

        AssignmentResponse testResponse = new AssignmentResponse(
                1L,
                1L,
                "Updated test",
                "Updated test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        Mockito.when(service.updateAssignment(lessonId, assignmentId, request)).thenReturn(testResponse);

        mockMvc.perform(patch("/lessons/{lessonId}/assignments/{assignmentId}", lessonId, assignmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lessonId").value(1L))
                .andExpect(jsonPath("$.name").value("Updated test"))
                .andExpect(jsonPath("$.description").value("Updated test description"))
                .andExpect(jsonPath("$.type").value(AssignmentType.TEST.name()))
                .andExpect(jsonPath("$.status").value(AssignmentStatus.ASSIGNED.name()))
                .andExpect(jsonPath("$.maxScore").value(100))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updateAssignment(lessonId, assignmentId, request);
    }

    @Test
    @DisplayName("Endpoint getAssignment should return response")
    void getAssignment_shouldReturnResponse() throws Exception {
        Long lessonId = 1L;
        Long assignmentId = 1L;

        AssignmentResponse testResponse = new AssignmentResponse(
                1L,
                1L,
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        Mockito.when(service.getAssignment(lessonId, assignmentId)).thenReturn(testResponse);

        mockMvc.perform(get("/lessons/{lessonId}/assignments/{assignmentId}", lessonId, assignmentId))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lessonId").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.type").value(AssignmentType.TEST.name()))
                .andExpect(jsonPath("$.status").value(AssignmentStatus.ASSIGNED.name()))
                .andExpect(jsonPath("$.maxScore").value(100))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).getAssignment(lessonId, assignmentId);
    }

    @Test
    @DisplayName("Endpoint deleteAssignment should delete assignment")
    void deleteAssignment_shouldDeleteAssignment() throws Exception {
        Long lessonId = 1L;
        Long assignmentId = 1L;

        mockMvc.perform(delete("/lessons/{lessonId}/assignments/{assignmentId}", lessonId, assignmentId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteAssignment(lessonId, assignmentId);
    }

    @Test
    @DisplayName("Endpoint getAssignmentsByLessonId should return list of assignments")
    void getAssignmentsByLessonId_shouldReturnList_ofAssignments() throws Exception {
        Long lessonId = 1L;

        List<AssignmentShortResponse> responses = List.of(
                new AssignmentShortResponse(
                    1L,
                    "Test 1",
                    AssignmentType.TEST,
                    AssignmentStatus.ASSIGNED,
                    100,
                    1
                ),
                new AssignmentShortResponse(
                    1L,
                    "Test 2",
                    AssignmentType.THEORY,
                    AssignmentStatus.OVERDUE,
                    80,
                    2
                )
        );

        Mockito.when(service.getAssignmentsByLessonId(lessonId)).thenReturn(responses);

        mockMvc.perform(get("/lessons/{lessonId}/assignments", lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test 1"))
                .andExpect(jsonPath("$[1].name").value("Test 2"));

        Mockito.verify(service, Mockito.times(1)).getAssignmentsByLessonId(lessonId);
    }

    @Test
    @DisplayName("Endpoint countAssignmentsByLessonId should return count of assignments")
    void countAssignmentsByLessonId_shouldReturnCount_ofAssignments() throws Exception {
        Long lessonId = 1L;

        Mockito.when(service.countAssignmentsByLessonId(lessonId)).thenReturn(2);

        mockMvc.perform(get("/lessons/{lessonId}/assignments/count", lessonId))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        Mockito.verify(service, Mockito.times(1)).countAssignmentsByLessonId(lessonId);
    }

}
