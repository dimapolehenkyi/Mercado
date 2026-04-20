package com.example.mercado.courses.course.courseLearningPoint.service;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapper;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.utils.EntityFinder;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("CoursePublicLP Service Impl Test")
public class CoursePublicLearningPointServiceImplTest {

    @Mock
    private CourseLearningPointRepository repository;

    @Mock
    private CourseLearningPointMapper mapper;

    private CoursePublicLearningPointServiceImpl service;

    @BeforeEach
    void setUp() {
        EntityFinder finder = new EntityFinder();

        service = new CoursePublicLearningPointServiceImpl(
                mapper, repository, finder
        );
    }

    @Test
    @DisplayName("getCourseLearningPoint should return correct LearningPoint when exists")
    void getCourseLearningPoint_shouldReturnCorrectLearningPoint_whenExists() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        LearningPointResponse response = new LearningPointResponse(
                1L,
                1L,
                "Test text",
                1
        );

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenReturn(Optional.of(point));
        Mockito.when(mapper.toResponse(point))
                .thenReturn(response);

        LearningPointResponse answer = service.getCourseLearningPoint(1L, 1L);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(answer),
                () -> Assertions.assertEquals(answer, response)
        );
    }

    @Test
    @DisplayName("getCourseLearningPoint should throw when not exists")
    void getCourseLearningPoint_shouldThrow_whenNotExists() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(2L)
                .build();

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenThrow(
                    new AppException(ErrorCode.LEARNING_POINT_NOT_FOUND)
                );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.getCourseLearningPoint(1L, 1L)
        );

        Assertions.assertEquals(ErrorCode.LEARNING_POINT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("getCourseLearningPoints should return correct list when exists")
    void getCourseLearningPoints_shouldReturnCorrectList_whenExists() {
        List<CourseLearningPoint> points = List.of(
                CourseLPTestFactory.createDefaultCourseLP()
                        .id(1L)
                        .text("Test text 1")
                        .position(1)
                        .build(),
                CourseLPTestFactory.createDefaultCourseLP()
                        .id(2L)
                        .text("Test text 2")
                        .position(2)
                        .build(),
                CourseLPTestFactory.createDefaultCourseLP()
                        .id(3L)
                        .text("Test text 3")
                        .position(3)
                        .build()
        );

        List<LearningPointResponse> responses = List.of(
                new LearningPointResponse(
                        1L,
                        1L,
                        "Test text 1",
                        1
                ),
                new LearningPointResponse(
                        2L,
                        1L,
                        "Test text 2",
                        2
                ),
                new LearningPointResponse(
                        3L,
                        1L,
                        "Test text 3",
                        3
                )
        );

        Mockito.when(repository.findAllByCourseIdOrderByPositionAsc(1L))
                .thenReturn(points);
        Mockito.when(mapper.toResponse(points.get(0)))
                .thenReturn(responses.get(0));
        Mockito.when(mapper.toResponse(points.get(1)))
                .thenReturn(responses.get(1));
        Mockito.when(mapper.toResponse(points.get(2)))
                .thenReturn(responses.get(2));

        List<LearningPointResponse> answer = service.getCourseLearningPoints(1L);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(answer),
                () -> Assertions.assertEquals(answer, responses)
        );
    }

    @Test
    @DisplayName("getCourseLearningPoints should return empty list when no one exists")
    void getCourseLearningPoints_shouldReturnEmptyList_whenNoOneExists() {
        List<LearningPointResponse> responses = List.of(
                new LearningPointResponse(
                        1L,
                        1L,
                        "Test text 1",
                        1
                ),
                new LearningPointResponse(
                        2L,
                        1L,
                        "Test text 2",
                        2
                ),
                new LearningPointResponse(
                        3L,
                        1L,
                        "Test text 3",
                        3
                )
        );

        Mockito.when(repository.findAllByCourseIdOrderByPositionAsc(1L))
                .thenReturn(List.of());

        List<LearningPointResponse> answer = service.getCourseLearningPoints(1L);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(answer),
                () -> Assertions.assertTrue(answer.isEmpty())
        );

        Mockito.verifyNoInteractions(mapper);
    }

}
