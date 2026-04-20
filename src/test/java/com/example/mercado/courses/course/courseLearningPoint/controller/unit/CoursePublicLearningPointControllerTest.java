package com.example.mercado.courses.course.courseLearningPoint.controller.unit;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseLearningPoint.controller.CoursePublicLearningPointController;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CoursePublicLearningPointService;
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

@WebMvcTest(CoursePublicLearningPointController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CoursePublicLP Controller Test")
public class CoursePublicLearningPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoursePublicLearningPointService service;

    @Test
    @DisplayName("getLearningPoint should return LearningPoint when point exists")
    void getLearningPoint_shouldReturnLearningPoint_whenPointExists() throws Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        LearningPointResponse response = new LearningPointResponse(
                1L,
                1L,
                "test text",
                1
        );

        Mockito.when(service.getCourseLearningPoint(courseId, pointId))
                .thenReturn(response);

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.position").value(1));

        Mockito.verify(service, Mockito.times(1))
                .getCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("getLearningPoint should return {NOT_FOUND} when point doesnt exists")
    void getLearningPoint_shouldReturnNotFound_whenPointDoesNotExist() throws Exception {
        Long courseId = 1L;
        Long pointId = 1L;

        Mockito.when(service.getCourseLearningPoint(courseId, pointId))
                .thenThrow(new AppException(ErrorCode.LEARNING_POINT_NOT_FOUND));

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getLearningPoint should throw when courseId negative")
    void getLearningPoint_shouldThrow_whenCourseIdNegative() throws Exception {
        Long courseId = -1L;
        Long pointId = 1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("getAllByCourseId should throw when pointId negative")
    void getLearningPoint_shouldThrow_whenPointIdNegative() throws Exception {
        Long courseId = 1L;
        Long pointId = -1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}", courseId, pointId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getCourseLearningPoint(courseId, pointId);
    }

    @Test
    @DisplayName("getAllByCourseId should return list of LearningPoints when exist")
    void getAllByCourseId_shouldReturnLearningPoints_whenPointsExist() throws Exception {
        Long courseId = 1L;

        List<LearningPointResponse> responses = List.of(
                new LearningPointResponse(
                        1L,
                        1L,
                        "test text 1",
                        1
                ),
                new LearningPointResponse(
                        2L,
                        1L,
                        "test text 2",
                        2
                ),
                new LearningPointResponse(
                        3L,
                        1L,
                        "test text 3",
                        3
                )
        );

        Mockito.when(service.getCourseLearningPoints(1L))
                .thenReturn(responses);

        mockMvc.perform(get("/courses/{courseId}/learning-points", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))

                .andExpect(jsonPath("$[0].text").value("test text 1"))
                .andExpect(jsonPath("$[0].id").value(1L))

                .andExpect(jsonPath("$[1].text").value("test text 2"))
                .andExpect(jsonPath("$[1].id").value(2L))

                .andExpect(jsonPath("$[2].text").value("test text 3"))
                .andExpect(jsonPath("$[2].id").value(3L));

        Mockito.verify(service, Mockito.times(1)).getCourseLearningPoints(courseId);
    }

    @Test
    @DisplayName("getCourseRequirement should return empty list when no points exist")
    void getAllByCourseId_shouldReturnEmptyList_whenNoPointsExist() throws Exception {
        Long courseId = 1L;

        List<LearningPointResponse> responses = List.of();

        Mockito.when(service.getCourseLearningPoints(1L))
                .thenReturn(responses);

        mockMvc.perform(get("/courses/{courseId}/learning-points", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(service, Mockito.times(1)).getCourseLearningPoints(courseId);
    }

    @Test
    @DisplayName("getCourseRequirement should throw when courseId negative")
    void getAllByCourseId_shouldThrow_whenCourseIdNegative() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getCourseLearningPoints(courseId);
    }

}
