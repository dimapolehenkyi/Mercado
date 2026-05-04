package com.example.mercado.integration.courses.courseLearningPoint.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CoursePublicLearningPointService;
import com.example.mercado.testUtils.courses.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CoursePublicLearningPointServiceImpl Integration Test")
@ActiveProfiles("test")
public class CoursePublicLearningPointServiceImplTest {

    @Autowired
    private CourseLearningPointRepository repository;

    @Autowired
    private CoursePublicLearningPointService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }


    @Test
    @DisplayName("getCourseLearningPoint should return correct LearningPoint when exists")
    void getCourseLearningPoint_shouldReturnLearningPoint_whenPointExists() {
        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("test")
                        .build()
        );

        LearningPointResponse response = service.getCourseLearningPoint(
                1L,
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertNotNull(response),
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("test", response.text()),
                () -> Assertions.assertEquals(1, response.position())
        );
    }

    @Test
    @DisplayName("getCourseLearningPoint should throw {NOT_FOUND} when point doesnt exists")
    void getCourseLearningPoint_shouldThrowNotFoundException_whenPointDoesNotExist() {
        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.getCourseLearningPoint(1L, 1L)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("getCourseLearningPoints should return correct ordered LearningPoints when course has")
    void getCourseLearningPoints_shouldReturnOrderedPoints_whenCourseHasLearningPoints() {
        List<CourseLearningPoint> points = List.of(
                repository.save(
                        CourseLPTestFactory.createDefaultCourseLP()
                                .text("test 1")
                                .position(1)
                                .build()
                ),
                repository.save(
                        CourseLPTestFactory.createDefaultCourseLP()
                                .text("test 3")
                                .position(3)
                                .build()
                ),
                repository.save(
                        CourseLPTestFactory.createDefaultCourseLP()
                                .text("test 2")
                                .position(2)
                                .build()
                )
        );

        List<LearningPointResponse> responses = service.getCourseLearningPoints(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertNotNull(responses),
                () -> Assertions.assertEquals(3, responses.size()),

                () -> Assertions.assertEquals(1, responses.get(0).position()),
                () -> Assertions.assertEquals("test 1", responses.get(0).text()),

                () -> Assertions.assertEquals(2, responses.get(1).position()),
                () -> Assertions.assertEquals("test 2", responses.get(1).text()),

                () -> Assertions.assertEquals(3, responses.get(2).position()),
                () -> Assertions.assertEquals("test 3", responses.get(2).text())
        );
    }

    @Test
    @DisplayName("getCourseLearningPoints should return empty list when course hasn't no one")
    void getCourseLearningPoints_shouldReturnEmptyList_whenCourseHasNoLearningPoints() {
        List<LearningPointResponse> responses = service.getCourseLearningPoints(
                1L
        );

        Assertions.assertTrue(responses.isEmpty());
    }

}
