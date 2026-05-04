package com.example.mercado.unit.courses.course.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.publicService.CoursePublicServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("CoursePublicServiceImpl Test")
public class CoursePublicServiceImplTest {

    @Mock
    private CourseRepository repository;

    @Mock
    private CourseMapper mapper;

    private CoursePublicServiceImpl service;


    private Pageable pageable;



    @BeforeEach
    void setUp() {
        EntityFinder finder = new EntityFinder();

        service = new CoursePublicServiceImpl(repository, mapper, finder);

        pageable = PageRequest.of(0, 10);
    }



    @Test
    @DisplayName("Func getCoursesByStatus should return mapped pages correctly")
    void getCoursesByStatus_shouldReturnMappedPagesCorrectly() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );

        Mockito.when(repository.findByStatus(CourseStatus.PUBLISHED, pageable))
                .thenReturn(CourseTestData.page(course));
        Mockito.when(mapper.toShortResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToShortResponse(i.getArgument(0)));

        Page<CourseShortResponse> result = service.getCoursesByStatus(CourseStatus.PUBLISHED, pageable);

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("Java", result.getContent().getFirst().name());
    }

    @Test
    @DisplayName("Func getCoursesByStatus should return empty page")
    void getCoursesByStatus_shouldReturnEmptyPage() {
        Mockito.when(repository.findByStatus(CourseStatus.PUBLISHED, pageable))
                .thenReturn(Page.empty());

        Page<CourseShortResponse> result = service.getCoursesByStatus(CourseStatus.PUBLISHED, pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func getCoursesByStatus should return mapped pages correctly")
    void getMyCourse_shouldReturnMappedPagesCorrectly() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.userId(1L);
                }
        );

        Mockito.when(repository.findAllByUserId(1L, pageable))
                .thenReturn(CourseTestData.page(course));
        Mockito.when(mapper.toShortResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToShortResponse(i.getArgument(0)));

        Page<CourseShortResponse> result = service.getMyCourse(1L, pageable);

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("Java", result.getContent().getFirst().name());
    }

    @Test
    @DisplayName("Func getCoursesByStatus should return empty page")
    void getMyCourse_shouldReturnEmptyPage() {
        Mockito.when(repository.findAllByUserId(1L, pageable))
                .thenReturn(Page.empty());

        Page<CourseShortResponse> result = service.getMyCourse(1L, pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func getPopularCourses should return mapped pages correctly")
    void getPopularCourses_shouldReturnMappedPagesCorrectly() {
        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.studentCount(10L);
                }
        );

        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.studentCount(8L);
                }
        );

        Mockito.when(repository.findAllByOrderByStudentCountDesc(pageable))
                .thenReturn(CourseTestData.page(course1, course2));
        Mockito.when(mapper.toShortResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToShortResponse(i.getArgument(0)));

        Page<CourseShortResponse> result = service.getPopularCourses(pageable);

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Java", result.getContent().get(0).name());
        Assertions.assertEquals("Python", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("Func getPopularCourses should return empty page")
    void getPopularCourses_shouldReturnEmptyPage() {
        Mockito.when(repository.findAllByOrderByStudentCountDesc(pageable))
                .thenReturn(Page.empty());

        Page<CourseShortResponse> result = service.getPopularCourses(pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func getActiveCourseById should return course")
    void getActiveCourseById_shouldReturnCourse() {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );

        Mockito.when(repository.findActiveById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(mapper.toResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToDetailsResponse(i.getArgument(0)));

        CourseDetailsResponse result = service.getActiveCourseById(1L);

        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Func getActiveCourseById should throw then not found")
    void getActiveCourseById_shouldThrow_whenNotFound() {
        Mockito.when(repository.findActiveById(1L))
                .thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.getActiveCourseById(1L)
        );

        Assertions.assertEquals(ErrorCode.COURSE_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("Func getAllCourses should return mapped pages correctly")
    void getAllCourses_shouldReturnMappedPagesCorrectly() {
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

        Mockito.when(repository.findAll(pageable))
                .thenReturn(CourseTestData.page(course1, course2));
        Mockito.when(mapper.toShortResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToShortResponse(i.getArgument(0)));

        Page<CourseShortResponse> result = service.getAllCourses(pageable);

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Java", result.getContent().get(0).name());
        Assertions.assertEquals("Python", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("Func getAllCourses should return empty page")
    void getAllCourses_shouldReturnEmptyPage() {
        Mockito.when(repository.findAll(pageable))
                .thenReturn(Page.empty());

        Page<CourseShortResponse> result = service.getAllCourses(pageable);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func getCoursesByTeacher should return mapped pages correctly")
    void getCoursesByTeacher_shouldReturnMappedPagesCorrectly() {
        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.teacherId(1L);
                }
        );

        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Python");
                    a.teacherId(1L);
                }
        );

        Mockito.when(repository.findAllByTeacherId(1L, pageable))
                .thenReturn(CourseTestData.page(course1, course2));
        Mockito.when(mapper.toShortResponse(Mockito.any()))
                .thenAnswer(i -> CourseTestData.mapToShortResponse(i.getArgument(0)));

        Page<CourseShortResponse> result = service.getCoursesByTeacherId(1L, pageable);

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Java", result.getContent().get(0).name());
        Assertions.assertEquals("Python", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("Func getCoursesByTeacher should return empty page")
    void getCoursesByTeacher_shouldReturnEmptyPage() {
        Mockito.when(repository.findAllByTeacherId(1L, pageable))
                .thenReturn(Page.empty());

        Page<CourseShortResponse> result = service.getCoursesByTeacherId(1L, pageable);

        Assertions.assertTrue(result.isEmpty());
    }
}
