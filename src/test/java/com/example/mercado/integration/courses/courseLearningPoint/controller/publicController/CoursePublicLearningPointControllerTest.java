package com.example.mercado.integration.courses.courseLearningPoint.controller.publicController;

import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CoursePublicLearningPointService;
import com.example.mercado.testUtils.courses.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CoursePublicLearningPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseLearningPointRepository repository;

    @Autowired
    private CoursePublicLearningPointService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }


    @Test
    @DisplayName("getLearningPoint should return 200 and LearningPoint whenPointExists")
    void getLearningPoint_shouldReturn200AndLearningPoint_whenPointExists() throws  Exception {
        Long courseId = 1L;

        CourseLearningPoint point = repository.save(
                CourseLearningPoint.builder()
                        .courseId(1L)
                        .text("test text")
                        .position(1)
                        .build()
        );

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}",
                        courseId, point.getId()
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(point.getId()))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.position").value(1));
    }

    @Test
    @DisplayName("getLearningPoint should return 404 when LearningPoint doesnt exists")
    void getLearningPoint_shouldReturn404_whenLearningPointDoesNotExist() throws  Exception {
        Long courseId = 1L;

        Long pointId = 1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}",
                        courseId, pointId
                ))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getLearningPoint should return 400 when courseId negative")
    void getLearningPoint_shouldReturn400_whenCourseIdIsNotPositive() throws  Exception {
        Long courseId = -1L;
        Long pointId = 1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}",
                        courseId, pointId
                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getLearningPoint should return 400 when pointId negative")
    void getLearningPoint_shouldReturn400_whenPointIdIsNotPositive() throws  Exception {
        Long courseId = 1L;
        Long pointId = -1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points/{pointId}",
                        courseId, pointId
                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getAllByCourseId should return 200 and ordered list of LearningPoints when points exist")
    void getAllByCourseId_shouldReturn200AndOrderedLearningPoints_whenPointsExist() throws  Exception {
        Long courseId = 1L;

        List<CourseLearningPoint> responses = List.of(
                repository.save(
                        CourseLPTestFactory.createDefaultCourseLP()
                                .position(1)
                                .text("test text 1")
                                .courseId(courseId)
                                .build()
                ),
                repository.save(
                        CourseLPTestFactory.createDefaultCourseLP()
                                .position(3)
                                .text("test text 3")
                                .courseId(courseId)
                                .build()
                ),
                repository.save(
                        CourseLPTestFactory.createDefaultCourseLP()
                                .position(2)
                                .text("test text 2")
                                .courseId(courseId)
                                .build()
                )
        );

        mockMvc.perform(get("/courses/{courseId}/learning-points", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].text").value("test text 1"))
                .andExpect(jsonPath("$[1].text").value("test text 2"))
                .andExpect(jsonPath("$[2].text").value("test text 3"));
    }

    @Test
    @DisplayName("getAllByCourseId should return 200 and empty list when no one learning points exist")
    void getAllByCourseId_shouldReturn200AndEmptyList_whenNoLearningPointsExist() throws  Exception {
        Long courseId = 1L;

        List<CourseLearningPoint> responses = List.of();

        mockMvc.perform(get("/courses/{courseId}/learning-points", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("getAllByCourseId should return 400 when courseId negative")
    void getAllByCourseId_shouldReturn400_whenCourseIdIsNotPositive() throws  Exception {
        Long courseId = -1L;

        mockMvc.perform(get("/courses/{courseId}/learning-points",
                        courseId
                ))
                .andExpect(status().isBadRequest());
    }

}
