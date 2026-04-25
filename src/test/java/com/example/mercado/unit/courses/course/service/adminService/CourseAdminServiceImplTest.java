package com.example.mercado.unit.courses.course.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.CourseAdminServiceImpl;
import com.example.mercado.courses.course.utils.EntityFinder;
import com.example.mercado.testUtils.courses.course.CourseTestData;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("CourseAdminServiceImpl Test")
public class CourseAdminServiceImplTest {


    @Mock
    private CourseRepository repository;

    private EntityFinder finder;

    @Mock
    private CourseMapper mapper;

    private CourseAdminServiceImpl service;


    @BeforeEach
    void setUp() {
        finder = new EntityFinder();

        service = new CourseAdminServiceImpl(
                repository, mapper, finder
        );
    }


    @Test
    @DisplayName("Func createCourse should create course successfully")
    void createCourse_shouldCreateCourseSuccessfully() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        CreateCourseRequest request = CourseTestFactory.defaultCreateCourseRequest();

        Mockito.when(repository.existsByName("Java")).thenReturn(false);
        Mockito.when(mapper.toEntity(request)).thenReturn(course);
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToDetailsResponse(i.getArgument(0)));

        CourseDetailsResponse result = service.createCourse(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Java", result.name());
    }

    @Test
    @DisplayName("Func createCourse should throw when course already exists")
    void createCourse_shouldThrow_whenCourseAlreadyExists() {
        CreateCourseRequest request = CourseTestFactory.defaultCreateCourseRequest();

        Mockito.when(repository.existsByName("Java")).thenReturn(true);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_ALREADY_EXISTS, ex.getCode());
    }

    @Test
    @DisplayName("Func createCourse should apply pricing")
    void createCourse_shouldApplyPricing() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        CreateCourseRequest request = CourseTestFactory.defaultCreateCourseRequest();

        Mockito.when(repository.existsByName("Java")).thenReturn(false);
        Mockito.when(mapper.toEntity(request)).thenReturn(course);
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToDetailsResponse(i.getArgument(0)));

        service.createCourse(request);

        Assertions.assertEquals(request.type(), course.getType());
        Assertions.assertEquals(request.price(), course.getPrice());
    }

    @Test
    @DisplayName("Func updateCourse should update course successfully")
    void updateCourse_shouldUpdateSuccessfully() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        UpdateCourseRequest request = CourseTestFactory.defaultUpdateCourseRequest();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(repository.existsByName("New name")).thenReturn(false);
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToDetailsResponse(i.getArgument(0)));

        CourseDetailsResponse result = service.updateCourse(1L, request);

        Assertions.assertEquals("New name", result.name());
    }

    @Test
    @DisplayName("Func updateCourse should throw when not found")
    void updateCourse_shouldThrow_whenNotFound() {
        UpdateCourseRequest request = CourseTestFactory.defaultUpdateCourseRequest();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(1L, request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("Func updateCourse should throw when name exists")
    void updateCourse_shouldThrow_whenNameExists() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        UpdateCourseRequest request = CourseTestFactory.defaultUpdateCourseRequest();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(repository.existsByName("New name")).thenReturn(true);

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(1L, request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_ALREADY_EXISTS, ex.getCode());
    }

    @Test
    @DisplayName("Func updateCourse should apply pricing")
    void updateCourse_shouldApplyPricing() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        UpdateCourseRequest request = CourseTestFactory.defaultUpdateCourseRequest();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(repository.existsByName(Mockito.any())).thenReturn(false);
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToDetailsResponse(i.getArgument(0)));

        service.updateCourse(1L, request);

        Assertions.assertEquals(request.type(), course.getType());
        Assertions.assertEquals(request.price(), course.getPrice());
    }

    @Test
    @DisplayName("Func updateCourse should throw when name invalid")
    void updateCourse_shouldThrow_whenNameInvalid() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        UpdateCourseRequest request = CourseTestFactory.defaultUpdateCourseRequest();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(repository.existsByName(Mockito.any())).thenReturn(false);

        UpdateCourseRequest badRequest = new UpdateCourseRequest(
                "",
                "New description",
                "New short description",
                CourseAccessType.PAID,
                CourseLevel.ADVANCED,
                BigDecimal.valueOf(200L),
                "https://NewPreviewVideoUrl",
                "https://NewThumbnailUrl"
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(1L, badRequest)
        );

        Assertions.assertEquals(ErrorCode.COURSE_NAME_INVALID, ex.getCode());
    }

    @Test
    @DisplayName("Func changeCourseStatus should update status")
    void changeCourseStatus_shouldUpdateStatus() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));

        ChangeStatusRequest request = new ChangeStatusRequest(CourseStatus.PUBLISHED);

        service.changeCourseStatus(1L, request);

        Assertions.assertEquals(CourseStatus.PUBLISHED, course.getStatus());
    }

    @Test
    @DisplayName("Func changeCourseStatus should throw when not found")
    void changeCourseStatus_shouldThrow_whenNotFound() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        ChangeStatusRequest request = new ChangeStatusRequest(CourseStatus.PUBLISHED);

        Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseStatus(1L, request)
        );
    }

    @Test
    @DisplayName("Func changeCourseStatus should throw when invalid status")
    void changeCourseStatus_shouldThrow_whenInvalidStatus() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));

        ChangeStatusRequest request = new ChangeStatusRequest(null);

        Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseStatus(1L, request)
        );
    }

    @Test
    @DisplayName("Func changeCourseLevel should update level")
    void changeCourseLevel_shouldUpdateLevel() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));

        ChangeLevelRequest request = new ChangeLevelRequest(CourseLevel.ADVANCED);

        service.changeCourseLevel(1L, request);

        Assertions.assertEquals(CourseLevel.ADVANCED, course.getLevel());
    }

    @Test
    @DisplayName("Func changeCourseLevel should throw when not found")
    void changeCourseLevel_shouldThrow_whenNotFound() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        ChangeLevelRequest request = new ChangeLevelRequest(CourseLevel.ADVANCED);

        Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseLevel(1L, request)
        );
    }

    @Test
    @DisplayName("Func changeCourseLevel should throw when invalid level")
    void changeCourseLevel_shouldThrow_whenInvalidLevel() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));

        ChangeLevelRequest request = new ChangeLevelRequest(null);

        Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseLevel(1L, request)
        );
    }

    @Test
    @DisplayName("Func deleteCourse should close course")
    void deleteCourse_shouldCloseCourse() {
        Course course = CourseTestFactory.createDefaultCourse()
                .id(1L)
                .name("Java")
                .build();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(course));

        service.deleteCourse(1L);

        Assertions.assertEquals(CourseStatus.CLOSED, course.getStatus());
        Assertions.assertEquals(true, course.getDeleted());
    }

    @Test
    @DisplayName("Func deleteCourse should throw when not found")
    void deleteCourse_shouldThrow_whenNotFound() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                AppException.class,
                () -> service.deleteCourse(1L)
        );
    }

}
