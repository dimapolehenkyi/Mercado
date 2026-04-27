package com.example.mercado.unit.courses.courseRequirement.controller.adminController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseRequirement.controller.adminController.CourseAdminRequirementController;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.service.adminService.CourseAdminRequirementService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseAdminRequirementController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CourseAdminRequirement Controller Test")
public class CourseAdminRequirementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseAdminRequirementService service;



    @Test
    @DisplayName("createCourseRequirement should return 201 and Requirement when valid request")
    void createCourseRequirement_shouldReturn201AndRequirement_whenValidRequest() throws  Exception {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                "Test"
        );

        RequirementResponse response = new RequirementResponse(
                1L,
                1L,
                "Test",
                1
        );

        Mockito.when(service.createCourseRequirement(courseId, request))
                .thenReturn(response);

        mockMvc.perform(post("/admin/courses/{courseId}/requirements", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("Test"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).createCourseRequirement(courseId, request);
    }

    @Test
    @DisplayName("createCourseRequirement should return {400} when request invalid")
    void createCourseRequirement_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                null
        );

        mockMvc.perform(post("/admin/courses/{courseId}/requirements", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).createCourseRequirement(courseId, request);
    }

    @Test
    @DisplayName("createCourseRequirement should return {400} when courseId negative")
    void createCourseRequirement_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        AddRequirementRequest request = new AddRequirementRequest(
                null
        );

        mockMvc.perform(post("/admin/courses/{courseId}/requirements", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).createCourseRequirement(courseId, request);
    }

    @Test
    @DisplayName("updateCourseRequirement should return {200} and update Requirement when valid request")
    void updateCourseRequirement_shouldReturn200AndUpdatedRequirement_whenValidRequest() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        RequirementResponse response = new RequirementResponse(
                1L,
                1L,
                "Updated",
                1
        );

        Mockito.when(service.updateCourseRequirement(courseId, requirementId, request))
                .thenReturn(response);

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("Updated"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updateCourseRequirement(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updateCourseRequirement should return {400} when request invalid")
    void updateCourseRequirement_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                null
        );

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourseRequirement(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updateCourseRequirement should return {404} when Requirement {NOT_FOUND}}")
    void updateCourseRequirement_shouldReturn404_whenRequirementNotFound() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 100L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        Mockito.when(service.updateCourseRequirement(requirementId, courseId, request))
                .thenThrow(new AppException(ErrorCode.REQUIREMENT_NOT_FOUND));

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).updateCourseRequirement(requirementId, courseId, request);
    }

    @Test
    @DisplayName("updateCourseRequirement should return {400} when courseId negative")
    void updateCourseRequirement_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        Long requirementId = 1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourseRequirement(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updateCourseRequirement should return {400} when requirementId negative")
    void updateCourseRequirement_shouldReturn400_whenRequirementIdNegative() throws  Exception {
        Long courseId = 1L;
        Long requirementId = -1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourseRequirement(courseId, requirementId, request);
    }

    @Test
    @DisplayName("deleteCourseRequirement should return {204} when Requirement exists")
    void deleteCourseRequirement_shouldReturn204_whenRequirementExists() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        mockMvc.perform(delete("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("deleteCourseRequirement should return {404} when Requirement not found")
    void deleteCourseRequirement_shouldReturn404_whenRequirementNotFound() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        Mockito.doThrow(new AppException(ErrorCode.REQUIREMENT_NOT_FOUND))
                .when(service)
                .deleteCourseRequirement(courseId, requirementId);

        mockMvc.perform(delete("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).deleteCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("deleteCourseRequirement should return {400} when courseId negative")
    void deleteCourseRequirement_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        Long requirementId = 1L;

        mockMvc.perform(delete("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).deleteCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("deleteCourseRequirement should return {400} when requirementId negative")
    void deleteCourseRequirement_shouldReturn400_whenRequirementIdNegative() throws  Exception {
        Long courseId = 1L;
        Long requirementId = -1L;

        mockMvc.perform(delete("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).deleteCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("updatePosition should return {200} and update Requirement when valid request")
    void updatePosition_shouldReturn200AndUpdatedRequirement_whenValidRequest() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                1
        );

        RequirementResponse response = new RequirementResponse(
                1L,
                1L,
                "test text",
                1
        );

        Mockito.when(service.updatePosition(courseId, requirementId, request))
                .thenReturn(response);

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1)).updatePosition(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updatePosition should return {404} when Requirement {NOT_FOUND}")
    void updatePosition_shouldReturn404_whenRequirementNotFound() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                1
        );

        Mockito.when(service.updatePosition(courseId, requirementId, request))
                .thenThrow(new AppException(ErrorCode.LEARNING_POINT_NOT_FOUND));

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).updatePosition(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updatePosition should return {400} when request invalid")
    void updatePosition_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;
        Long requirementId = -1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                null
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updatePosition(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updatePosition should return {400} when courseId negative")
    void updatePosition_shouldReturn400_whenCourseIdNegative() throws  Exception {
        Long courseId = -1L;
        Long requirementId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                1
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updatePosition(courseId, requirementId, request);
    }

    @Test
    @DisplayName("updatePosition should return {400} when requirementId negative")
    void updatePosition_shouldReturn400_whenRequirementIdNegative() throws  Exception {
        Long courseId = 1L;
        Long requirementId = -1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                1
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updatePosition(courseId, requirementId, request);
    }

}
