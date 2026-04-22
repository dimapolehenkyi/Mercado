package com.example.mercado.courses.course.courseLearningPoint.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.testutils.course.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("CourseLP Repository Test")
public class CourseLearningPointRepositoryTest {

    @Autowired
    private CourseLearningPointRepository repository;

    @Test
    @DisplayName("existsByCourseIdAndText should return true when record exists")
    void existsByCourseIdAndText_shouldReturnTrue_whenExists() {
        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP().build()
        );

        boolean exists = repository.existsByCourseIdAndText(
                point.getCourseId(), point.getText()
        );

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("existsByCourseIdAndText should return false when record does not exist")
    void existsByCourseIdAndText_shouldReturnFalse_whenNotExists() {
        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP().build()
        );

        boolean exists = repository.existsByCourseIdAndText(
                10000L, point.getText()
        );

        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("findAllByCourseIdOrderByPositionAsc should return ordered list when data exists")
    void findAllByCourseIdOrderByPositionAsc_shouldReturnOrderedList_whenDataExists() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(2)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 2")
                        .build()
        );

        List<CourseLearningPoint> result = repository.findAllByCourseIdOrderByPositionAsc(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.size()),

                () -> Assertions.assertEquals(1, result.get(0).getPosition()),
                () -> Assertions.assertEquals(2, result.get(1).getPosition()),

                () -> Assertions.assertEquals("Test text 2", result.get(0).getText()),
                () -> Assertions.assertEquals("Test text 1",  result.get(1).getText())
        );
    }

    @Test
    @DisplayName("findAllByCourseIdOrderByPositionAsc should return empty list when no data exists")
    void findAllByCourseIdOrderByPositionAsc_shouldReturnEmptyList_whenNoDataExists() {
        List<CourseLearningPoint> result = repository.findAllByCourseIdOrderByPositionAsc(
                1L
        );

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAllByCourseIdOrderByPositionAsc should not return points from other courses")
    void findAllByCourseIdOrderByPositionAsc_shouldNotReturnOtherCoursesData() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(2)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 2")
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                .position(3)
                .courseId(2L)
                .text("Test text 3")
                .build()
        );

        List<CourseLearningPoint> result = repository.findAllByCourseIdOrderByPositionAsc(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.size()),

                () -> Assertions.assertEquals(1, result.get(0).getPosition()),
                () -> Assertions.assertEquals(2, result.get(1).getPosition()),

                () -> Assertions.assertEquals("Test text 2", result.get(0).getText()),
                () -> Assertions.assertEquals("Test text 1",  result.get(1).getText())
        );
    }

    @Test
    @DisplayName("findByIdAndCourseId should return entity when point exists in given course")
    void findByIdAndCourseId_shouldReturnEntity_whenExists() {
        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .build()
        );

        Optional<CourseLearningPoint> exists  = repository.findByIdAndCourseId(
                point.getId(), point.getCourseId()
        );

        Assertions.assertAll(
                () -> Assertions.assertTrue(exists.isPresent()),

                () -> Assertions.assertEquals("Test text", exists.get().getText()),
                () -> Assertions.assertEquals(1, exists.get().getPosition()),
                () -> Assertions.assertEquals(1L, exists.get().getCourseId())
        );
    }

    @Test
    @DisplayName("findByIdAndCourseId should return empty when point does not exist")
    void findByIdAndCourseId_shouldReturnEmpty_whenPointDoesNotExist() {
        Optional<CourseLearningPoint> exists  = repository.findByIdAndCourseId(
                100L, 100L
        );

        Assertions.assertTrue(exists.isEmpty());
    }

    @Test
    @DisplayName("countByCourseId should return correct count when points exist")
    void countByCourseId_shouldReturnCorrectCount_whenPointsExist() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(2)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 2")
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .courseId(2L)
                        .text("Test text 2")
                        .build()
        );

        int count = repository.countByCourseId(
                1L
        );

        Assertions.assertEquals(2, count);
    }

    @Test
    @DisplayName("countByCourseId should return zero when no points exist for course")
    void countByCourseId_shouldReturnZero_whenNoPointsExist() {
        int count = repository.countByCourseId(
                2L
        );

        Assertions.assertEquals(0, count);
    }

    @Test
    @DisplayName("deleteAllByCourseId should delete all points for given courseId")
    void deleteAllByCourseId_shouldDeleteAllPoints_whenCourseExists() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(2)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 2")
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .courseId(2L)
                        .text("Test text 2")
                        .build()
        );

        repository.deleteAllByCourseId(
                1L
        );

        Assertions.assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("deleteAllByCourseId should not fail when no points exist for courseId")
    void deleteAllByCourseId_shouldNotFail_whenNoPointsExist() {
        Assertions.assertDoesNotThrow(
                () -> repository.deleteAllByCourseId(1L)
        );
    }

    @Test
    @DisplayName("findMaxPositionByCourseId should return max position when points exist")
    void findMaxPositionByCourseId_shouldReturnMaxPosition_whenPointsExist() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(2)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 2")
                        .build()
        );

        int maxPosition = repository.findMaxPositionByCourseId(
                1L
        );

        Assertions.assertEquals(2, maxPosition);
    }

    @Test
    @DisplayName("findMaxPositionByCourseId should return -1 when no points exist for course")
    void findMaxPositionByCourseId_shouldReturnMinusOne_whenNoPointsExist() {
        int maxPosition = repository.findMaxPositionByCourseId(
                1L
        );

        Assertions.assertEquals(-1, maxPosition);
    }

    @Test
    @DisplayName("incrementPositionRange should increase positions in range when valid range provided")
    void incrementPositionRange_shouldIncreasePositions_whenValidRangeProvided() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(2)
                        .text("Test text 2")
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(3)
                        .text("Test text 3")
                        .build()
        );
        CourseLearningPoint point4 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(4)
                        .text("Test text 4")
                        .build()
        );
        CourseLearningPoint point5 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(5)
                        .courseId(2L)
                        .text("Test text 5")
                        .build()
        );

        repository.incrementPositionRange(
            1L,
                2,
                5
        );

        var result = repository.findAllByCourseIdOrderByPositionAsc(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.get(0).getPosition()),
                () -> Assertions.assertEquals(3, result.get(1).getPosition()),
                () -> Assertions.assertEquals(4, result.get(2).getPosition()),
                () -> Assertions.assertEquals(5, result.get(3).getPosition())
        );
    }

    @Test
    @DisplayName("decrementPositionRange should decrease positions in range when valid range provided")
    void decrementPositionRange_shouldDecreasePositions_whenValidRangeProvided() {
        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(1)
                        .text("Test text 1")
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(3)
                        .text("Test text 2")
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(4)
                        .text("Test text 3")
                        .build()
        );
        CourseLearningPoint point4 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(5)
                        .text("Test text 4")
                        .build()
        );
        CourseLearningPoint point5 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .position(6)
                        .courseId(2L)
                        .text("Test text 6")
                        .build()
        );

        repository.decrementPositionRange(
                1L,
                3,
                6
        );

        var result = repository.findAllByCourseIdOrderByPositionAsc(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.get(0).getPosition()),
                () -> Assertions.assertEquals(2, result.get(1).getPosition()),
                () -> Assertions.assertEquals(3, result.get(2).getPosition()),
                () -> Assertions.assertEquals(4, result.get(3).getPosition())
        );
    }
}
