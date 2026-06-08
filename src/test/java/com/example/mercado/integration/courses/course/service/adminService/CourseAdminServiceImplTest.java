package com.example.mercado.integration.courses.course.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.adminService.CourseAdminService;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CourseAdminServiceIntegrationTest")
@ActiveProfiles("test")
public class CourseAdminServiceImplTest {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseAdminService service;


    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should create correct course if valid request")
    void createCourse_shouldCreateCorrectCourse() {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = "Python";
                }
        );

        CourseDetailsResponse result = service.createCourse(request);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Python", result.name()),
                () -> Assertions.assertEquals("Test description", result.description()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(100), result.price()),
                () -> Assertions.assertEquals(CourseLevel.ADVANCED, result.level())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should throw exception when name already exists in database")
    void createCourse_shouldThrowException_whenNameAlreadyExist() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = "Python";
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_ALREADY_EXISTS, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should throw exception when name is null")
    void createCourse_shouldThrowException_whenNameIsNull() {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = null;
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_NAME_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should throw exception when name is blank")
    void createCourse_shouldThrowException_whenNameIsBlank() {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = "   ";
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_NAME_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should throw exception when type is null")
    void createCourse_shouldThrowException_whenTypeIsNotValid() {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.type = null;
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_ACCESS_TYPE_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should throw exception when price is null")
    void createCourse_shouldThrowException_whenPriceIsNull() {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.price = null;
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_PRICE_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourse should throw exception when price is less than zero")
    void createCourse_shouldThrowException_whenPriceIsLessThanZero() {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.price = BigDecimal.valueOf(-10L);
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.createCourse(request)
        );

        Assertions.assertEquals(ErrorCode.COURSE_PRICE_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should correctly update entity when request is valid")
    void updateCourse_shouldCorrectlyUpdateEntity_whenValidRequest() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.name = "Java";
                    a.price = BigDecimal.valueOf(1L);
                    a.level = CourseLevel.BEGINNER;
                }
        );

        CourseDetailsResponse result = service.updateCourse(
                course.getId(),
                request
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals("Java", result.name()),
                () -> Assertions.assertEquals(CourseLevel.BEGINNER, result.level()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(1L), result.price())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should throw exception when name already exists")
    void updateCourse_shouldThrow_whenNameAlreadyExists() {
        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.name = "Java";
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(
                        course1.getId(),
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_ALREADY_EXISTS, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should throw exception when name is null")
    void updateCourse_shouldThrowException_whenNameIsNull() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.name = null;
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(
                        course.getId(),
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_NAME_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should throw exception when name is blank")
    void updateCourse_shouldThrowException_whenNameIsBlank() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.name = "  ";
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(
                        course.getId(),
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_NAME_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should throw exception when type is null")
    void updateCourse_shouldThrowException_whenTypeIsNotValid() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.type = null;
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(
                        course.getId(),
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_ACCESS_TYPE_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should throw exception when price is null")
    void updateCourse_shouldThrowException_whenPriceIsNull() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.price = null;
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(
                        course.getId(),
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_PRICE_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourse should throw exception when price is less than zero")
    void updateCourse_shouldThrowException_whenPriceIsLessThanZero() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(
                a -> {
                    a.price = BigDecimal.valueOf(-10L);
                }
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourse(
                        course.getId(),
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_PRICE_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("changeCourseStatus should correctly change status if valid request")
    void changeCourseStatus_shouldCorrectlyChangeStatus_ifValidRequest() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.status(CourseStatus.DRAFT);
                }
        );

        repository.save(course);

        ChangeStatusRequest request = new ChangeStatusRequest(
                CourseStatus.PUBLISHED
        );

        service.changeCourseStatus(
                course.getId(),
                request
        );

        Optional<Course> updated = repository.findById(course.getId());

        Assertions.assertAll(
                () -> Assertions.assertTrue(updated.isPresent()),
                () -> Assertions.assertEquals(CourseStatus.PUBLISHED, updated.get().getStatus()),
                () -> Assertions.assertEquals("Python", updated.get().getName())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("changeCourseStatus should throw exception when course /NOT-FOUND/")
    void changeCourseStatus_shouldThrowException_whenCourseNotFound() {
        ChangeStatusRequest request = new ChangeStatusRequest(
                CourseStatus.PUBLISHED
        );

        Long courseId = 1L;

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseStatus(
                        courseId,
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_NOT_FOUND, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("changeCourseStatus should throw exception when status invalid")
    void changeCourseStatus_shouldThrowException_whenStatusInvalid() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.status(CourseStatus.CLOSED);
                }
        );

        repository.save(course);

        ChangeStatusRequest request = new ChangeStatusRequest(
                CourseStatus.PUBLISHED
        );

        Long courseId = 1L;

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseStatus(
                        courseId,
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_STATUS_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("changeCourseLevel should correctly change level if valid request")
    void changeCourseLevel_shouldCorrectlyChangeLevel_ifValidRequest() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.level(CourseLevel.BEGINNER);
                }
        );

        repository.save(course);

        ChangeLevelRequest request = new ChangeLevelRequest(
                CourseLevel.ADVANCED
        );

        service.changeCourseLevel(
                course.getId(),
                request
        );

        Optional<Course> updated = repository.findById(course.getId());

        Assertions.assertAll(
                () -> Assertions.assertTrue(updated.isPresent()),
                () -> Assertions.assertEquals(CourseLevel.ADVANCED, updated.get().getLevel()),
                () -> Assertions.assertEquals("Python", updated.get().getName())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("changeCourseLevel should throw exception when course /NOT-FOUND/")
    void changeCourseLevel_shouldThrowException_whenCourseNotFound() {
        ChangeLevelRequest request = new ChangeLevelRequest(
                CourseLevel.ADVANCED
        );

        Long courseId = 1L;

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseLevel(
                        courseId,
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_NOT_FOUND, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("changeCourseLevel should throw exception when level is null")
    void changeCourseLevel_shouldThrowException_whenLevelIsNull() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.level(CourseLevel.BEGINNER);
                }
        );

        repository.save(course);

        ChangeLevelRequest request = new ChangeLevelRequest(
                null
        );

        Long courseId = 1L;

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.changeCourseLevel(
                        courseId,
                        request
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_LEVEL_INVALID, ex.getCode());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourse should correctly delete course when valid request (change status and flag deleted=true)")
    void deleteCourse_shouldCorrectlyDeleteCourse_whenValidRequest() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.status(CourseStatus.PUBLISHED);
                    a.deleted(false);
                }
        );

        repository.save(course);

        service.deleteCourse(
                course.getId()
        );

        Optional<Course> deleted = repository.findById(course.getId());

        Assertions.assertAll(
                () -> Assertions.assertTrue(deleted.isPresent()),
                () -> Assertions.assertEquals("Python", deleted.get().getName()),
                () -> Assertions.assertEquals(CourseStatus.CLOSED, deleted.get().getStatus()),
                () -> Assertions.assertTrue(deleted.get().getDeleted())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourse should throw exception when course /NOT-FOUND/")
    void deleteCourse_shouldThrowException_whenCourseNotFound() {
        Long courseId = 1L;

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.deleteCourse(
                        courseId
                )
        );

        Assertions.assertEquals(ErrorCode.COURSE_NOT_FOUND, ex.getCode());
    }

}
