package com.example.mercado.unit.courses.courseRequirement.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapper;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.publicService.CoursePublicRequirementServiceImpl;
import com.example.mercado.courses.course.utils.EntityFinder;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("CoursePublicRequirement Service Impl Test")
public class CoursePublicRequirementServiceImplTest {

    @Mock
    private CourseRequirementRepository repository;

    @Mock
    private CourseRequirementMapper mapper;

    private CoursePublicRequirementServiceImpl service;

    @BeforeEach
    void setUp() {
        EntityFinder finder = new EntityFinder();

        service = new CoursePublicRequirementServiceImpl(
                repository, mapper, finder
        );
    }


    @Test
    @DisplayName("getCourseRequirement should return correct Requirement when exists")
    void getCourseRequirement_shouldReturnCorrectRequirement_whenExists() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        RequirementResponse response = new RequirementResponse(
                1L,
                1L,
                "Test text",
                1
        );

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenReturn(Optional.of(requirement));
        Mockito.when(mapper.toResponse(requirement))
                .thenReturn(response);

        RequirementResponse answer = service.getCourseRequirement(1L, 1L);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(answer),
                () -> Assertions.assertEquals(answer, response)
        );
    }

    @Test
    @DisplayName("getCourseRequirement should throw when not exists")
    void getCourseRequirement_shouldThrow_whenNotExists() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(2L)
                .build();

        Mockito.when(repository.findByIdAndCourseId(1L, 1L))
                .thenThrow(
                        new AppException(ErrorCode.REQUIREMENT_NOT_FOUND)
                );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.getCourseRequirement(1L, 1L)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("getAllByCourseId should return correct list when exists")
    void getAllByCourseId_shouldReturnCorrectList_whenExists() {
        List<CourseRequirement> requirements = List.of(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .id(1L)
                        .text("Test text 1")
                        .position(1)
                        .build(),
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .id(2L)
                        .text("Test text 2")
                        .position(2)
                        .build(),
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .id(3L)
                        .text("Test text 3")
                        .position(3)
                        .build()
        );

        List<RequirementResponse> responses = List.of(
                new RequirementResponse(
                        1L,
                        1L,
                        "Test text 1",
                        1
                ),
                new RequirementResponse(
                        2L,
                        1L,
                        "Test text 2",
                        2
                ),
                new RequirementResponse(
                        3L,
                        1L,
                        "Test text 3",
                        3
                )
        );

        Mockito.when(repository.findAllByCourseIdOrderByPositionAsc(1L))
                .thenReturn(requirements);
        Mockito.when(mapper.toResponse(requirements.get(0)))
                .thenReturn(responses.get(0));
        Mockito.when(mapper.toResponse(requirements.get(1)))
                .thenReturn(responses.get(1));
        Mockito.when(mapper.toResponse(requirements.get(2)))
                .thenReturn(responses.get(2));

        List<RequirementResponse> answer = service.getAllByCourseId(1L);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(answer),
                () -> Assertions.assertEquals(answer, responses)
        );
    }

    @Test
    @DisplayName("getAllByCourseId should return empty list when no one exists")
    void getAllByCourseId_shouldReturnEmptyList_whenNoOneExists() {
        List<RequirementResponse> responses = List.of(
                new RequirementResponse(
                        1L,
                        1L,
                        "Test text 1",
                        1
                ),
                new RequirementResponse(
                        2L,
                        1L,
                        "Test text 2",
                        2
                ),
                new RequirementResponse(
                        3L,
                        1L,
                        "Test text 3",
                        3
                )
        );

        Mockito.when(repository.findAllByCourseIdOrderByPositionAsc(1L))
                .thenReturn(List.of());

        List<RequirementResponse> answer = service.getAllByCourseId(1L);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(answer),
                () -> Assertions.assertTrue(answer.isEmpty())
        );

        Mockito.verifyNoInteractions(mapper);
    }

}
