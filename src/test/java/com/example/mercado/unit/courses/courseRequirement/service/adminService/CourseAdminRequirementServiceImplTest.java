package com.example.mercado.unit.courses.courseRequirement.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapper;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.adminService.CourseAdminRequirementServiceImpl;
import com.example.mercado.courses.course.utils.EntityFinder;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestData;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestFactory;
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
@DisplayName("CourseAdminRequirement Service Impl Test")
public class CourseAdminRequirementServiceImplTest {


    @Mock
    private CourseRequirementRepository repository;

    @Mock
    private CourseRequirementMapper mapper;

    private CourseAdminRequirementServiceImpl service;

    @BeforeEach
    void setUp() {
        EntityFinder finder = new EntityFinder();

        service = new CourseAdminRequirementServiceImpl(
                repository, mapper, finder
        );
    }


    @Test
    @DisplayName("createCourseRequirement should create requirement when valid request")
    void createCourseRequirement_shouldCreateRequirement_whenValidRequest() {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                "Test text"
        );
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(false);
        Mockito.when(repository.findMaxPositionByCourseId(courseId))
                .thenReturn(2);
        Mockito.when(mapper.toEntity(courseId, request))
                .thenReturn(requirement);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseRequirementTestData.mapToRequirementResponse(i.getArgument(0)));
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        RequirementResponse response = service.createCourseRequirement(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test text", response.text()),
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals(3, response.position())
        );
    }

    @Test
    @DisplayName("createCourseRequirement should throw when duplicate text exists")
    void createCourseRequirement_shouldThrow_whenDuplicateTextExists() {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                "Test text"
        );

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(true);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourseRequirement(1L, request)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_ALREADY_EXISTS, ex.getCode());
    }

    @Test
    @DisplayName("createCourseRequirement should throw when limit reached")
    void createCourseRequirement_shouldThrow_whenLimitReached() {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                "Test text"
        );

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(false);
        Mockito.when(repository.findMaxPositionByCourseId(courseId))
                .thenReturn(10);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourseRequirement(1L, request)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_LIMIT_REACHED, ex.getCode());
    }

    @Test
    @DisplayName("createCourseRequirement should set correct position when first point")
    void createCourseRequirement_shouldSetCorrectPosition_whenFirstPoint() {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                "Test text"
        );
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        Mockito.when(repository.existsByCourseIdAndText(courseId, request.text()))
                .thenReturn(false);
        Mockito.when(repository.findMaxPositionByCourseId(courseId))
                .thenReturn(0);
        Mockito.when(mapper.toEntity(courseId, request))
                .thenReturn(requirement);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseRequirementTestData.mapToRequirementResponse(i.getArgument(0)));
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        RequirementResponse response = service.createCourseRequirement(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test text", response.text()),
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals(1, response.position())
        );
    }

    @Test
    @DisplayName("updateCourseRequirement should update text when valid request")
    void updateCourseRequirement_shouldUpdateText_whenValidRequest() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated text"
        );

        Mockito.when(repository.findByIdAndCourseId(requirement.getId(), requirement.getCourseId()))
                .thenReturn(Optional.of(requirement));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseRequirementTestData.mapToRequirementResponse(i.getArgument(0)));

        RequirementResponse response = service.updateCourseRequirement(
                1L,
                1L,
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(requirement.getId(), response.id()),
                () -> Assertions.assertEquals(requirement.getCourseId(), response.courseId()),
                () -> Assertions.assertEquals(requirement.getText(), response.text()),
                () -> Assertions.assertEquals(requirement.getPosition(), response.position())
        );
    }

    @Test
    @DisplayName("updateCourseRequirement should throw when point not found")
    void updateCourseRequirement_shouldThrow_whenPointNotFound() {
        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated text"
        );

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourseRequirement(1L, 1L, request)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("deleteCourseRequirement should delete requirement when exists")
    void deleteCourseRequirement_shouldDeleteRequirement_whenExists() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(2L)
                .text("Test text 2")
                .position(2)
                .build();

        Mockito.when(repository.findByIdAndCourseId(requirement.getId(), requirement.getCourseId()))
                .thenReturn(Optional.of(requirement));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);

        service.deleteCourseRequirement(2L, 1L);

        Mockito.verify(repository).decrementPositionRange(1L, 3, 3);
        Mockito.verify(repository).delete(requirement);
    }

    @Test
    @DisplayName("deleteCourseRequirement should throw when requirement not found")
    void deleteCourseRequirement_shouldThrow_whenRequirementNotFound() {
        ReorderRequirementRequest request = new ReorderRequirementRequest(
                2
        );

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updatePosition(1L, 1L, request)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("updatePosition should do nothing when same position")
    void updatePosition_shouldDoNothing_whenSamePosition() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .position(2)
                .build();

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                2
        );

        Mockito.when(repository.findByIdAndCourseId(requirement.getId(), requirement.getCourseId()))
                .thenReturn(Optional.of(requirement));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseRequirementTestData.mapToRequirementResponse(i.getArgument(0)));

        RequirementResponse response = service.updatePosition(
                1L,
                1L,
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(requirement.getId(), response.id()),
                () -> Assertions.assertEquals(requirement.getCourseId(), response.courseId()),
                () -> Assertions.assertEquals(requirement.getText(), response.text()),
                () -> Assertions.assertEquals(2, response.position())
        );
    }

    @Test
    @DisplayName("updatePosition should throw when position invalid")
    void updatePosition_shouldThrow_whenPositionInvalid() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .position(2)
                .build();

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                4
        );

        Mockito.when(repository.findByIdAndCourseId(requirement.getId(), requirement.getCourseId()))
                .thenReturn(Optional.of(requirement));
        Mockito.when(repository.findMaxPositionByCourseId(1L))
                .thenReturn(3);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updatePosition(1L, 1L, request)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_POSITION_INVALID, ex.getCode());
    }

}
