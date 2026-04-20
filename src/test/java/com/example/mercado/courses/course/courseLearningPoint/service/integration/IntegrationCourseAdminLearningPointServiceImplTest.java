package com.example.mercado.courses.course.courseLearningPoint.service.integration;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import com.example.mercado.courses.testutils.course.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class IntegrationCourseAdminLearningPointServiceImplTest {

    @Autowired
    private CourseLearningPointRepository repository;

    @Autowired
    private CourseAdminLearningPointService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourseLearningPoint should set correct last position when some points already exists")
    void createCourseLearningPoint_shouldSetCorrectLastPosition_whenSomePointsAlreadyExists() {
        Long courseId = 1L;

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );

        AddLearningPointRequest request = new AddLearningPointRequest(
                "New point"
        );

        LearningPointResponse response = service.createCourseLearningPoint(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(4L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("New point", response.text()),
                () -> Assertions.assertEquals(4, response.position())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourseLearningPoint should set correct first position when no one points exists")
    void createCourseLearningPoint_shouldSetCorrectFirstPosition_whenNoOnePointsExists() {
        Long courseId = 1L;

        AddLearningPointRequest request = new AddLearningPointRequest(
                "New point"
        );

        LearningPointResponse response = service.createCourseLearningPoint(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("New point", response.text()),
                () -> Assertions.assertEquals(0, response.position())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourseLearningPoint should shift position after deletion")
    void deleteCourseLearningPoint_shouldShiftPositionsAfterDeletion() {
        Long courseId = 1L;

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );

        service.deleteCourseLearningPoint(2L, 1L);

        List<CourseLearningPoint> points = repository.findAllByCourseIdOrderByPositionAsc(1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, points.size()),
                () -> Assertions.assertEquals(1, points.get(0).getPosition()),
                () -> Assertions.assertEquals(2, points.get(1).getPosition())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePositionCourseLearningPoint should correct shift position")
    void updatePositionCourseLearningPoint_shouldCorrectShiftPositions() {
        Long courseId = 1L;

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point4 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 4")
                        .position(4)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point5 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 5")
                        .position(5)
                        .courseId(courseId)
                        .build()
        );

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                5L,
                2
        );

        service.updatePositionCourseLearningPoint(
                5L,
                1L,
                request
        );

        List<CourseLearningPoint> points = repository.findAllByCourseIdOrderByPositionAsc(1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, points.size()),

                () -> Assertions.assertEquals(1, points.get(0).getPosition()),
                () -> Assertions.assertEquals("Test text 1", points.get(0).getText()),

                () -> Assertions.assertEquals(2, points.get(1).getPosition()),
                () -> Assertions.assertEquals("Test text 5", points.get(1).getText()),

                () -> Assertions.assertEquals(3, points.get(2).getPosition()),
                () -> Assertions.assertEquals("Test text 2", points.get(2).getText()),

                () -> Assertions.assertEquals(4, points.get(3).getPosition()),
                () -> Assertions.assertEquals("Test text 3", points.get(3).getText()),

                () -> Assertions.assertEquals(5, points.get(4).getPosition()),
                () -> Assertions.assertEquals("Test text 4", points.get(4).getText())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePositionCourseLearningPoint should correct up position")
    void updatePositionCourseLearningPoint_shouldCorrectUpPositions() {
        Long courseId = 1L;

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );
        CourseLearningPoint point4 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test text 4")
                        .position(4)
                        .courseId(courseId)
                        .build()
        );

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                2L,
                4
        );

        service.updatePositionCourseLearningPoint(
                2L,
                1L,
                request
        );

        List<CourseLearningPoint> points = repository.findAllByCourseIdOrderByPositionAsc(1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(4, points.size()),

                () -> Assertions.assertEquals(1, points.get(0).getPosition()),
                () -> Assertions.assertEquals("Test text 1", points.get(0).getText()),

                () -> Assertions.assertEquals(2, points.get(1).getPosition()),
                () -> Assertions.assertEquals("Test text 3", points.get(1).getText()),

                () -> Assertions.assertEquals(3, points.get(2).getPosition()),
                () -> Assertions.assertEquals("Test text 4", points.get(2).getText()),

                () -> Assertions.assertEquals(4, points.get(3).getPosition()),
                () -> Assertions.assertEquals("Test text 2", points.get(3).getText())
        );
    }

}
