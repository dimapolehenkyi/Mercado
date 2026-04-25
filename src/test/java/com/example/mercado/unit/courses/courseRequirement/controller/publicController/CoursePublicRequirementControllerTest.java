package com.example.mercado.unit.courses.courseRequirement.controller.publicController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseRequirement.controller.publicController.CoursePublicRequirementController;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.service.interfaces.CoursePublicRequirementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoursePublicRequirementController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CoursePublicRequirement Controller Test")
public class CoursePublicRequirementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoursePublicRequirementService service;


    @Test
    @DisplayName("getCourseRequirement should return Requirement when point exists")
    void getCourseRequirement_shouldReturnRequirement_whenPointExists() throws Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        RequirementResponse response = new RequirementResponse(
                1L,
                1L,
                "test text",
                1
        );

        Mockito.when(service.getCourseRequirement(courseId, requirementId))
                .thenReturn(response);

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1))
                .getCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("getCourseRequirement should return {NOT_FOUND} when requirement doesnt exists")
    void getCourseRequirement_shouldReturnNotFound_whenRequirementDoesNotExist() throws Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        Mockito.when(service.getCourseRequirement(courseId, requirementId))
                .thenThrow(new AppException(ErrorCode.REQUIREMENT_NOT_FOUND));

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getCourseRequirement should throw when courseId negative")
    void getCourseRequirement_shouldThrow_whenCourseIdNegative() throws Exception {
        Long courseId = -1L;
        Long requirementId = 1L;

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("getCourseRequirement should throw when requirementId negative")
    void getCourseRequirement_shouldThrow_whenRequirementIdNegative() throws Exception {
        Long courseId = 1L;
        Long requirementId = -1L;

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}", courseId, requirementId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getCourseRequirement(courseId, requirementId);
    }

    @Test
    @DisplayName("getAllByCourseId should return list of LearningPoints when exist")
    void getAllByCourseId_shouldReturnLearningPoints_whenPointsExist() throws Exception {
        Long courseId = 1L;

        List<RequirementResponse> responses = List.of(
                new RequirementResponse(
                        1L,
                        1L,
                        "test text 1",
                        1
                ),
                new RequirementResponse(
                        2L,
                        1L,
                        "test text 2",
                        2
                ),
                new RequirementResponse(
                        3L,
                        1L,
                        "test text 3",
                        3
                )
        );

        Mockito.when(service.getAllByCourseId(1L))
                .thenReturn(responses);

        mockMvc.perform(get("/courses/{courseId}/requirements", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))

                .andExpect(jsonPath("$[0].text").value("test text 1"))
                .andExpect(jsonPath("$[0].id").value(1L))

                .andExpect(jsonPath("$[1].text").value("test text 2"))
                .andExpect(jsonPath("$[1].id").value(2L))

                .andExpect(jsonPath("$[2].text").value("test text 3"))
                .andExpect(jsonPath("$[2].id").value(3L));

        Mockito.verify(service, Mockito.times(1)).getAllByCourseId(courseId);
    }

    @Test
    @DisplayName("getAllByCourseId should return empty list when no points exist")
    void getAllByCourseId_shouldReturnEmptyList_whenNoPointsExist() throws Exception {
        Long courseId = 1L;

        List<RequirementResponse> responses = List.of();

        Mockito.when(service.getAllByCourseId(1L))
                .thenReturn(responses);

        mockMvc.perform(get("/courses/{courseId}/requirements", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(service, Mockito.times(1)).getAllByCourseId(courseId);
    }

    @Test
    @DisplayName("getAllByCourseId should throw when courseId negative")
    void getAllByCourseId_shouldThrow_whenCourseIdNegative() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(get("/courses/{courseId}/requirements", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getAllByCourseId(courseId);
    }

}
