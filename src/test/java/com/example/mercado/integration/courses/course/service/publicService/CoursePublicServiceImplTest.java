package com.example.mercado.integration.courses.course.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseSearchFilter;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.enums.SortType;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.publicService.CoursePublicService;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CoursePublicService Integration Test")
public class CoursePublicServiceImplTest extends AbstractRepositoryTest {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CoursePublicService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @DisplayName("getMyCourse should return user's courses when user has courses")
    void getMyCourse_shouldReturnUserCourses_whenUserHasCourses() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Course published1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.userId(userId);
                    a.status(CourseStatus.PUBLISHED);
                    a.deleted(false);
                }
        );
        Course published2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.userId(userId);
                    a.status(CourseStatus.PUBLISHED);
                    a.deleted(false);
                }
        );
        Course draft = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.userId(userId);
                    a.status(CourseStatus.DRAFT);
                    a.deleted(false);
                }
        );
        Course deleted = CourseTestFactory.createCourse(
                a -> {
                    a.name("C#");
                    a.userId(userId);
                    a.status(CourseStatus.CLOSED);
                    a.deleted(true);
                }
        );

        repository.saveAll(
                List.of(
                        published1,
                        published2,
                        draft,
                        deleted
                )
        );

        Page<CourseShortResponse> result = service.getMyCourse(
                userId,
                pageable
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getContent().size()),
                () -> Assertions.assertEquals("Python", result.getContent().getFirst().name()),
                () -> Assertions.assertEquals("Java", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("getMyCourse should return empty page when user hasn't courses")
    void getMyCourse_shouldReturnEmptyPage_whenUserHasNoCourses() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Course draft = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.userId(userId);
                    a.status(CourseStatus.DRAFT);
                    a.deleted(false);
                }
        );
        Course deleted = CourseTestFactory.createCourse(
                a -> {
                    a.name("C#");
                    a.userId(userId);
                    a.status(CourseStatus.CLOSED);
                    a.deleted(true);
                }
        );

        repository.saveAll(
                List.of(
                        draft,
                        deleted
                )
        );

        Page<CourseShortResponse> result = service.getMyCourse(
                userId,
                pageable
        );

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getPopularCourses should return courses sorted by /STUDENT-COUNT/ desc")
    void getPopularCourses_shouldReturnCoursesSortedByStudentCountDesc() {
        Pageable pageable = PageRequest.of(0, 10);

        Course published1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.status(CourseStatus.PUBLISHED);
                    a.deleted(false);
                    a.studentCount(10L);
                }
        );
        Course published2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.status(CourseStatus.PUBLISHED);
                    a.deleted(false);
                    a.studentCount(20L);
                }
        );
        Course draft = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.status(CourseStatus.DRAFT);
                    a.deleted(false);
                    a.studentCount(80L);
                }
        );
        Course deleted = CourseTestFactory.createCourse(
                a -> {
                    a.name("C#");
                    a.status(CourseStatus.CLOSED);
                    a.deleted(true);
                    a.studentCount(30L);
                }
        );

        repository.saveAll(
                List.of(
                        published1,
                        published2,
                        draft,
                        deleted
                )
        );

        Page<CourseShortResponse> result = service.getPopularCourses(
                pageable
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getContent().size()),
                () -> Assertions.assertEquals("Python", result.getContent().getFirst().name()),
                () -> Assertions.assertEquals("Java", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("getPopularCourses should return empty page when no one courses exist")
    void getPopularCourses_shouldReturnEmptyPage_whenNoCoursesExist() {
        Pageable pageable = PageRequest.of(0, 10);

        Course draft = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.status(CourseStatus.DRAFT);
                    a.deleted(false);
                    a.studentCount(80L);
                }
        );
        Course deleted = CourseTestFactory.createCourse(
                a -> {
                    a.name("C#");
                    a.status(CourseStatus.CLOSED);
                    a.deleted(true);
                    a.studentCount(30L);
                }
        );

        repository.saveAll(
                List.of(
                        draft,
                        deleted
                )
        );

        Page<CourseShortResponse> result = service.getPopularCourses(
                pageable
        );

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getActiveCourseById should return course by ID when course is /ACTIVE/")
    void getActiveCourseById_shouldReturnCourse_whenCourseIsActive() {
        Course active = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.status(CourseStatus.PUBLISHED);
                    a.deleted(false);
                }
        );
        Course disActive = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.status(CourseStatus.CLOSED);
                    a.deleted(true);
                }
        );

        repository.saveAll(
                List.of(
                        active,
                        disActive
                )
        );

        CourseDetailsResponse result = service.getActiveCourseById(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals("Java", result.name())
        );
    }

    @Test
    @DisplayName("getActiveCourseById should throw exception when course /NOT-FOUND/")
    void getActiveCourseById_shouldThrowException_whenCourseNotFound() {
        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.getActiveCourseById(1L)
        );

        Assertions.assertEquals(ErrorCode.COURSE_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("getAllCourses should return paged courses when courses exist")
    void getAllCourses_shouldReturnPagedCourses_whenCoursesExist() {
        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );
        Course course3 = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                }
        );

        repository.saveAll(
                List.of(course1, course2, course3)
        );

        Page<CourseShortResponse> result = service.getAllCourses(pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),
                () -> Assertions.assertEquals(3, result.getTotalElements())
        );
    }

    @Test
    @DisplayName("getAllCourses should return empty page when no one courses exist")
    void getAllCourses_shouldReturnEmptyPage_whenNoCoursesExist() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<CourseShortResponse> result = service.getAllCourses(pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getCoursesByTeacherId should return courses when teacher has courses")
    void getCoursesByTeacherId_shouldReturnCourses_whenTeacherHasCourses() {
        Pageable pageable = PageRequest.of(0, 2);
        Long teacherId = 1L;

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(teacherId);
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(teacherId);
                }
        );
        Course course3 = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.teacherId(2L);
                }
        );
        repository.saveAll(
                List.of(course1, course2, course3)
        );

        Page<CourseShortResponse> result = service.getCoursesByTeacherId(
                teacherId,
                pageable
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),
                () -> Assertions.assertEquals(2, result.getTotalElements())
        );
    }

    @Test
    @DisplayName("getCoursesByTeacherId should return empty page when teacher hasn't courses")
    void getCoursesByTeacherId_shouldReturnEmptyPage_whenTeacherHasNoCourses() {
        Pageable pageable = PageRequest.of(0, 2);
        Long teacherId = 1L;

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(2L);
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(2L);
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.getCoursesByTeacherId(
                teacherId,
                pageable
        );

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("searchCourse should return filtered courses when valid filter")
    void searchCourse_shouldReturnFilteredCourses_whenValidFilter() {
        CourseSearchFilter filter = new CourseSearchFilter(
                null,
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(10L),
                BigDecimal.valueOf(50L),
                CourseLevel.ADVANCED,
                SortType.PRICE_ASC,
                CourseStatus.PUBLISHED
        );

        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(1L);
                    a.price(BigDecimal.valueOf(10L));
                    a.status(CourseStatus.PUBLISHED);
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(1L);
                    a.price(BigDecimal.valueOf(30L));
                    a.status(CourseStatus.PUBLISHED);
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.searchCourse(filter, pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(1, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),

                () -> Assertions.assertEquals("Java", result.getContent().get(0).name()),
                () -> Assertions.assertEquals("Python", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("searchCourse should return empty page when no one courses match filter")
    void searchCourse_shouldReturnEmptyPage_whenNoCoursesMatchFilter() {
        CourseSearchFilter filter = new CourseSearchFilter(
                null,
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(10L),
                BigDecimal.valueOf(50L),
                CourseLevel.ADVANCED,
                SortType.PRICE_ASC,
                CourseStatus.PUBLISHED
        );

        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(2L);
                    a.price(BigDecimal.valueOf(10L));
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(2L);
                    a.price(BigDecimal.valueOf(30L));
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.searchCourse(filter, pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("searchCourse should sort by /PRICE-ASC/ when sort type /PRICE-ASC/")
    void searchCourse_shouldSortByPriceAsc_whenSortTypePriceAsc() {
        CourseSearchFilter filter = new CourseSearchFilter(
                null,
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(10L),
                BigDecimal.valueOf(50L),
                CourseLevel.ADVANCED,
                SortType.PRICE_ASC,
                CourseStatus.PUBLISHED
        );

        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(1L);
                    a.price(BigDecimal.valueOf(10L));
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(1L);
                    a.price(BigDecimal.valueOf(30L));
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.searchCourse(filter, pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(1, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),

                () -> Assertions.assertEquals("Java", result.getContent().get(0).name()),
                () -> Assertions.assertEquals("Python", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("searchCourse should sort by /PRICE-DESC/ when sort type /PRICE-DESC/")
    void searchCourse_shouldSortByPriceDesc_whenSortTypePriceDesc() {
        CourseSearchFilter filter = new CourseSearchFilter(
                null,
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(10L),
                BigDecimal.valueOf(50L),
                CourseLevel.ADVANCED,
                SortType.PRICE_DESC,
                CourseStatus.PUBLISHED
        );

        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(1L);
                    a.price(BigDecimal.valueOf(10L));
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(1L);
                    a.price(BigDecimal.valueOf(30L));
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.searchCourse(filter, pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(1, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),

                () -> Assertions.assertEquals("Python", result.getContent().get(0).name()),
                () -> Assertions.assertEquals("Java", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("searchCourse should sort by /RATING-ASC/ when sort type /RATING-ASC/")
    void searchCourse_shouldSortByRatingAsc_whenSortTypeRatingAsc() {
        CourseSearchFilter filter = new CourseSearchFilter(
                null,
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(10L),
                BigDecimal.valueOf(50L),
                CourseLevel.ADVANCED,
                SortType.RATING_ASC,
                CourseStatus.PUBLISHED
        );

        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(1L);
                    a.rating(10.0);
                    a.price(BigDecimal.valueOf(10L));
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(1L);
                    a.rating(20.0);
                    a.price(BigDecimal.valueOf(30L));
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.searchCourse(filter, pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(1, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),

                () -> Assertions.assertEquals("Java", result.getContent().get(0).name()),
                () -> Assertions.assertEquals("Python", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("searchCourse should sort by /RATING-DESC/ when sort type /RATING-DESC/")
    void searchCourse_shouldSortByRatingDesc_whenSortTypeRatingDesc() {
        CourseSearchFilter filter = new CourseSearchFilter(
                null,
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(10L),
                BigDecimal.valueOf(50L),
                CourseLevel.ADVANCED,
                SortType.RATING_DESC,
                CourseStatus.PUBLISHED
        );

        Pageable pageable = PageRequest.of(0, 2);

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(1L);
                    a.rating(10.0);
                    a.price(BigDecimal.valueOf(10L));
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(1L);
                    a.rating(20.0);
                    a.price(BigDecimal.valueOf(30L));
                }
        );

        repository.saveAll(
                List.of(course1, course2)
        );

        Page<CourseShortResponse> result = service.searchCourse(filter, pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(1, result.getTotalPages()),
                () -> Assertions.assertEquals(2, result.getContent().size()),

                () -> Assertions.assertEquals("Python", result.getContent().get(0).name()),
                () -> Assertions.assertEquals("Java", result.getContent().get(1).name())
        );
    }

    @Test
    @DisplayName("countAllCourses should return correct count course's when courses exist")
    void countAllCourses_shouldReturnCorrectCount_whenCoursesExist() {
        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                }
        );
        Course course3 = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                }
        );

        repository.saveAll(
                List.of(course1, course2, course3)
        );

        long result = service.countAllCourses();

        Assertions.assertEquals(3, result);
    }

    @Test
    @DisplayName("countAllCourses should return zero when no one courses exist")
    void countAllCourses_shouldReturnZero_whenNoCoursesExist() {
        long result = service.countAllCourses();

        Assertions.assertEquals(0, result);
    }

    @Test
    @DisplayName("countTeacherCoursesById should return correct count when teacher has courses")
    void countTeacherCoursesById_shouldReturnCorrectCount_whenTeacherHasCourses() {
        Long teacherId = 1L;

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(teacherId);
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(teacherId);
                }
        );
        Course course3 = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.teacherId(2L);
                }
        );

        repository.saveAll(
                List.of(course1, course2, course3)
        );

        long result = service.countTeacherCoursesById(teacherId);

        Assertions.assertEquals(2, result);
    }

    @Test
    @DisplayName("countTeacherCoursesById should return zero when teacher hasn't courses")
    void countTeacherCoursesById_shouldReturnZero_whenTeacherHasNoCourses() {
        Long teacherId = 1L;

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(2L);
                }
        );
        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(2L);
                }
        );
        Course course3 = CourseTestFactory.createCourse(
                a -> {
                    a.name("C++");
                    a.teacherId(2L);
                }
        );

        long result = service.countTeacherCoursesById(teacherId);

        Assertions.assertEquals(0, result);
    }

}
