package com.example.mercado.unit.courses.courseLearningPoint.controller.adminController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseLearningPoint.controller.adminController.CourseAdminLearningPointController;
import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseAdminLearningPointController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CourseAdminLP Controller Test")
public class CourseAdminLearningPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseAdminLearningPointService service;

    @Test
    @DisplayName("addCourseLearningPoint should return 201 and LearningPoint when valid request")
    void addCourseLearningPoint_shouldReturn201AndLearningPoint_whenValidRequest() throws  Exception {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test"
        );

        LearningPointResponse response = new LearningPointResponse(
                1L,
                1L,
                "Test",
                1
        );

        Mockito.when(service.createCourseLearningPoint(courseId, request))
                .thenReturn(response);

        mockMvc.perform(post("/courses/{courseId}/admin/learning-points", courseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("Test"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).createCourseLearningPoint(courseId, request);
    }

    @Test
    @DisplayName("addCourseLearningPoint should return {400} when request invalid")
    void addCourseLearningPoint_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                null
        );

        mockMvc.perform(post("/courses/{courseId}/admin/learning-points", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).createCourseLearningPoint(courseId, request);
    }

    @Test
    @DisplayName("addCourseLearningPoint should return {400} when courseId negative")
    void addCourseLearningPoint_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                null
        );

        mockMvc.perform(post("/courses/{courseId}/admin/learning-points", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).createCourseLearningPoint(courseId, request);
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {200} and update LearningPoint when valid request")
    void updateCourseLearningPoint_shouldReturn200AndUpdatedLearningPoint_whenValidRequest() throws  Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        LearningPointResponse response = new LearningPointResponse(
                1L,
                1L,
                "Updated",
                1
        );

        Mockito.when(service.updateCourseLearningPoint(courseId, pointId, request))
                .thenReturn(response);

        mockMvc.perform(put("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("Updated"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updateCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {400} when request invalid")
    void updateCourseLearningPoint_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                null
        );

        mockMvc.perform(put("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {404} when LearningPoint {NOT_FOUND}}")
    void updateCourseLearningPoint_shouldReturn404_whenLearningPointNotFound() throws  Exception {
        Long courseId = 1L;
        Long pointId = 100L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        Mockito.when(service.updateCourseLearningPoint(courseId, pointId, request))
                .thenThrow(new AppException(ErrorCode.LEARNING_POINT_NOT_FOUND));

        mockMvc.perform(put("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).updateCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {400} when courseId negative")
    void updateCourseLearningPoint_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        Long pointId = 1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        mockMvc.perform(put("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {400} when pointId negative")
    void updateCourseLearningPoint_shouldReturn400_whenPointIdNegative() throws  Exception {
        Long courseId = 1L;
        Long pointId = -1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        mockMvc.perform(put("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should return {204} when LearningPoint exists")
    void deleteCourseLearningPoint_shouldReturn204_whenLearningPointExists() throws  Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        mockMvc.perform(delete("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should return {404} when LearningPoint not found")
    void deleteCourseLearningPoint_shouldReturn404_whenLearningPointNotFound() throws  Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        Mockito.doThrow(new AppException(ErrorCode.LEARNING_POINT_NOT_FOUND))
                .when(service)
                .deleteCourseLearningPoint(courseId, pointId);

        mockMvc.perform(delete("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).deleteCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should return {400} when courseId negative")
    void deleteCourseLearningPoint_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        Long pointId = 1L;

        mockMvc.perform(delete("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).deleteCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should return {400} when pointId negative")
    void deleteCourseLearningPoint_shouldReturn400_whenPointIdNegative() throws  Exception {
        Long courseId = 1L;
        Long pointId = -1L;

        mockMvc.perform(delete("/courses/{courseId}/admin/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).deleteCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("updatePosition should return {200} and update LearningPoint when valid request")
    void updatePosition_shouldReturn200AndUpdatedLearningPoint_whenValidRequest() throws  Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1
        );

        LearningPointResponse response = new LearningPointResponse(
                1L,
                1L,
                "test text",
                1
        );

        Mockito.when(service.updatePositionCourseLearningPoint(courseId, pointId, request))
                .thenReturn(response);

        mockMvc.perform(patch("/courses/{courseId}/admin/learning-points/{pointId}/position", courseId, pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updatePositionCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updatePosition should return {404} when LearningPoint {NOT_FOUND}")
    void updatePosition_shouldReturn404_whenLearningPointNotFound() throws  Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1
        );

        Mockito.when(service.updatePositionCourseLearningPoint(courseId, pointId, request))
                .thenThrow(new AppException(ErrorCode.LEARNING_POINT_NOT_FOUND));

        mockMvc.perform(patch("/courses/{courseId}/admin/learning-points/{pointId}/position", courseId, pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).updatePositionCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updatePosition should return {400} when request invalid")
    void updatePosition_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;
        Long pointId = -1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                null
        );

        mockMvc.perform(patch("/courses/{courseId}/admin/learning-points/{pointId}/position", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updatePositionCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updatePosition should return {400} when courseId negative")
    void updatePosition_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        Long pointId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1
        );

        mockMvc.perform(patch("/courses/{courseId}/admin/learning-points/{pointId}/position", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updatePositionCourseLearningPoint(courseId, pointId, request);
    }

    @Test
    @DisplayName("updatePosition should return {400} when pointId negative")
    void updatePosition_shouldReturn400_whenPointIdNegative() throws  Exception {
        Long courseId = 1L;
        Long pointId = -1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1
        );

        mockMvc.perform(patch("/courses/{courseId}/admin/learning-points/{pointId}/position", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updatePositionCourseLearningPoint(courseId, pointId, request);
    }
}
