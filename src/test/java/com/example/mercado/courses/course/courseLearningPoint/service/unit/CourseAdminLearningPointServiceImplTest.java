package com.example.mercado.courses.course.courseLearningPoint.service.unit;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapper;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.CourseAdminLearningPointServiceImpl;
import com.example.mercado.courses.course.utils.EntityFinder;
import com.example.mercado.courses.testutils.course.courseLearningPoint.CourseLPTestData;
import com.example.mercado.courses.testutils.course.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("CourseAdminLP Service Impl Test")
public class CourseAdminLearningPointServiceImplTest {

    @Mock
    private CourseLearningPointRepository repository;

    @Mock
    private CourseLearningPointMapper mapper;

    private CourseAdminLearningPointServiceImpl service;

    @BeforeEach
    void setUp() {
        EntityFinder finder = new EntityFinder();

        service = new CourseAdminLearningPointServiceImpl(
                mapper, repository, finder
        );
    }

    @Test
    @DisplayName("createCourseLearningPoint should create point when valid request")
    void createCourseLearningPoint_shouldCreatePoint_whenValidRequest() {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test text"
        );
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(false);
        Mockito.when(repository.findMaxPositionByCourseId(courseId))
                .thenReturn(2);
        Mockito.when(mapper.toEntity(courseId, request))
                .thenReturn(point);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseLPTestData.mapToLearningPointResponse(i.getArgument(0)));
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        LearningPointResponse response = service.createCourseLearningPoint(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test text", response.text()),
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals(3, response.position())
        );
    }

    @Test
    @DisplayName("createCourseLearningPoint should throw when duplicate text exists")
    void createCourseLearningPoint_shouldThrow_whenDuplicateTextExists() {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test text"
        );

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(true);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourseLearningPoint(1L, request)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_ALREADY_EXISTS, ex.getCode());
    }

    @Test
    @DisplayName("createCourseLearningPoint should throw when limit reached")
    void createCourseLearningPoint_shouldThrow_whenLimitReached() {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test text"
        );

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(false);
        Mockito.when(repository.findMaxPositionByCourseId(courseId))
                .thenReturn(10);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourseLearningPoint(1L, request)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_LIMIT_REACHED, ex.getCode());
    }

    @Test
    @DisplayName("createCourseLearningPoint should set correct position when first point")
    void createCourseLearningPoint_shouldSetCorrectPosition_whenFirstPoint() {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test text"
        );
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(false);
        Mockito.when(repository.findMaxPositionByCourseId(courseId))
                .thenReturn(0);
        Mockito.when(mapper.toEntity(courseId, request))
                .thenReturn(point);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseLPTestData.mapToLearningPointResponse(i.getArgument(0)));
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        LearningPointResponse response = service.createCourseLearningPoint(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test text", response.text()),
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals(1, response.position())
        );
    }

    @Test
    @DisplayName("updateCourseLearningPoint should")
    void updateCourseLearningPoint_shouldUpdateText_whenValidRequest() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated text"
        );

        Mockito.when(repository.findByIdAndCourseId(point.getId(), point.getCourseId()))
                .thenReturn(Optional.of(point));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseLPTestData.mapToLearningPointResponse(i.getArgument(0)));

        LearningPointResponse response = service.updateCourseLearningPoint(
                1L,
                1L,
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(point.getId(), response.id()),
                () -> Assertions.assertEquals(point.getCourseId(), response.courseId()),
                () -> Assertions.assertEquals(point.getText(), response.text()),
                () -> Assertions.assertEquals(point.getPosition(), response.position())
        );
    }

    @Test
    @DisplayName("updateCourseLearningPoint should")
    void updateCourseLearningPoint_shouldThrow_whenPointNotFound() {
        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated text"
        );

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourseLearningPoint(1L, 1L, request)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should")
    void deleteCourseLearningPoint_shouldDeletePoint_whenExists() {
        CourseLearningPoint point2 = CourseLPTestFactory.createDefaultCourseLP()
                .id(2L)
                .text("Test text 2")
                .position(2)
                .build();

        Mockito.when(repository.findByIdAndCourseId(point2.getId(), point2.getCourseId()))
                .thenReturn(Optional.of(point2));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);

        service.deleteCourseLearningPoint(2L, 1L);

        Mockito.verify(repository).decrementPositionRange(1L, 3, 3);
        Mockito.verify(repository).delete(point2);
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should")
    void deleteCourseLearningPoint_shouldThrow_whenPointNotFound() {
        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1L,
                2
        );

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updatePositionCourseLearningPoint(1L, 1L, request)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("updateCourseLearningPoint should")
    void updatePositionCourseLearningPoint_shouldHandleMoveToFirstPosition() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .position(2)
                .build();

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1L,
                1
        );

        Mockito.when(repository.findByIdAndCourseId(point.getId(), point.getCourseId()))
                .thenReturn(Optional.of(point));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseLPTestData.mapToLearningPointResponse(i.getArgument(0)));

        LearningPointResponse response = service.updatePositionCourseLearningPoint(
                1L,
                1L,
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(point.getId(), response.id()),
                () -> Assertions.assertEquals(point.getCourseId(), response.courseId()),
                () -> Assertions.assertEquals(point.getText(), response.text()),
                () -> Assertions.assertEquals(1, response.position())
        );
    }

    @Test
    @DisplayName("updateCourseLearningPoint should")
    void updatePositionCourseLearningPoint_shouldHandleMoveToLastPosition() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .position(2)
                .build();

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1L,
                3
        );

        Mockito.when(repository.findByIdAndCourseId(point.getId(), point.getCourseId()))
                .thenReturn(Optional.of(point));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseLPTestData.mapToLearningPointResponse(i.getArgument(0)));

        LearningPointResponse response = service.updatePositionCourseLearningPoint(
                1L,
                1L,
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(point.getId(), response.id()),
                () -> Assertions.assertEquals(point.getCourseId(), response.courseId()),
                () -> Assertions.assertEquals(point.getText(), response.text()),
                () -> Assertions.assertEquals(3, response.position())
        );
    }

    @Test
    @DisplayName("updatePositionCourseLearningPoint should")
    void updatePositionCourseLearningPoint_shouldDoNothing_whenSamePosition() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .position(2)
                .build();

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1L,
                2
        );

        Mockito.when(repository.findByIdAndCourseId(point.getId(), point.getCourseId()))
                .thenReturn(Optional.of(point));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseLPTestData.mapToLearningPointResponse(i.getArgument(0)));

        LearningPointResponse response = service.updatePositionCourseLearningPoint(
                1L,
                1L,
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(point.getId(), response.id()),
                () -> Assertions.assertEquals(point.getCourseId(), response.courseId()),
                () -> Assertions.assertEquals(point.getText(), response.text()),
                () -> Assertions.assertEquals(2, response.position())
        );
    }

    @Test
    @DisplayName("updatePositionCourseLearningPoint should")
    void updatePositionCourseLearningPoint_shouldThrow_whenPositionInvalid() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .position(2)
                .build();

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1L,
                4
        );

        Mockito.when(repository.findByIdAndCourseId(point.getId(), point.getCourseId()))
                .thenReturn(Optional.of(point));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updatePositionCourseLearningPoint(1L, 1L, request)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_POSITION_INVALID, ex.getCode());
    }
}
