package com.example.mercado.courses.course.service;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.exception.CourseAlreadyArchivedException;
import com.example.mercado.courses.course.exception.CourseAlreadyExistsException;
import com.example.mercado.courses.course.exception.CourseAlreadyPublishedException;
import com.example.mercado.courses.course.exception.CourseNotFound;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.testutils.course.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseServiceImpl Test")
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;


    @Test
    @DisplayName("Func createCourse should return CourseResponse")
    void createCourse_shouldReturnCourseResponse() {
        CreateCourseRequest request = CourseTestFactory.createTestCourseRequest();

        Course course = new Course();
        course.setName(request.name());

        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(false);
        Mockito.when(courseMapper.toEntity(request)).thenReturn(course);
        Mockito.when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(courseMapper.toResponse(any(Course.class)))
                .thenReturn(new CourseResponse(
                        null,
                        null,
                        "Test",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ));

        CourseResponse savedCourse = courseService.createCourse(request);
        Assertions.assertNotNull(savedCourse);
        Assertions.assertEquals(savedCourse.name(), request.name());
    }

    @Test
    @DisplayName("Func createCourse should throw exception when course exists")
    void createCourse_shouldThrowException_whenCourseExists() {
        CreateCourseRequest request = CourseTestFactory.createTestCourseRequest();

        Course course = new Course();
        course.setName(request.name());

        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(true);
        Mockito.verify(courseRepository, Mockito.never()).save(any(Course.class));

        assertThrows(CourseAlreadyExistsException.class, () -> courseService.createCourse(request));
    }


    @Test
    @DisplayName("Func createCourse should set price ZERO when status FREE")
    void createCourse_shouldSetPriceZero_whenStatusFree() {
        CreateCourseRequest request = CourseTestFactory.createTestCourseRequestFree();

        Course course = new Course();
        course.setType(CourseAccessType.FREE);

        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(false);
        Mockito.when(courseMapper.toEntity(request)).thenReturn(course);
        Mockito.when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(courseMapper.toResponse(any(Course.class)))
                .thenAnswer(i -> {
                    Course c = i.getArgument(0);
                    return new CourseResponse(
                            null,
                            c.getTeacherId(),
                            c.getName(),
                            c.getDescription(),
                            c.getPrice(),
                            c.getStatus(),
                            c.getType(),
                            c.getDurationInMinutes(),
                            c.getCreatedAt(),
                            c.getUpdatedAt()
                    );
                });
        CourseResponse response = courseService.createCourse(request);

        Assertions.assertEquals(BigDecimal.ZERO, response.price());
    }

    @Test
    @DisplayName("Func updateCourse should return course response with updated data")
    void updateCourse_shouldReturnCourseResponse() {
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();
        Course course = CourseTestFactory.createTestCourse(1L, 2L, "Test");

        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(false);
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Mockito.doAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            UpdateCourseRequest r = invocation.getArgument(1);
            c.setName(r.name());
            return null;
        }).when(courseMapper).updateEntity(course, request);

        Mockito.when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        Mockito.when(courseMapper.toResponse(any(Course.class)))
                .thenAnswer(i -> {
                    Course c = i.getArgument(0);
                    return new CourseResponse(
                            null,
                            c.getTeacherId(),
                            c.getName(),
                            c.getDescription(),
                            c.getPrice(),
                            c.getStatus(),
                            c.getType(),
                            c.getDurationInMinutes(),
                            c.getCreatedAt(),
                            c.getUpdatedAt()
                    );
                });

        CourseResponse response = courseService.updateCourse(1L, request);

        Assertions.assertEquals(request.name(), response.name());
    }

    @Test
    @DisplayName("Func updateCourse should throw exception when course exists")
    void updateCourse_shouldThrowException_whenCourseExists() {
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();

        Course course = CourseTestFactory.createTestCourse(1L, 2L, "Old name");

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(true);
        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());

        assertThrows(CourseAlreadyExistsException.class, () -> courseService.updateCourse(1L, request));
    }

    @Test
    @DisplayName("Func updateCourse should set price zero when status is free")
    void updateCourse_shouldSetPriceZero_whenStatusFree() {
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();

        Course course = new Course();
        course.setName("Old name");
        course.setType(CourseAccessType.FREE);

        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(false);
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        Mockito.doNothing().when(courseMapper).updateEntity(course, request);

        courseService.updateCourse(1L, request);

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(courseRepository).save(captor.capture());

        Assertions.assertEquals(BigDecimal.ZERO, captor.getValue().getPrice());
    }

    @Test
    @DisplayName("Func publishCourse should set type PUBLISHED if course exists")
    void publishCourse_shouldSetTypePublished() {
        Course course = CourseTestFactory
                .createTestCourseDraft(
                        1L,
                        2L,
                        "Old name"
                );

        Mockito.when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(courseRepository.save(any(Course.class)))
                .thenAnswer(i -> i.getArgument(0));

        courseService.publishCourse(1L);

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(courseRepository).save(captor.capture());

        Course savedCourse = captor.getValue();
        Assertions.assertEquals(CourseStatus.PUBLISHED, savedCourse.getStatus());
    }

    @Test
    @DisplayName("Func publishCourse should throw exception when course doesn't exists")
    void publishCourse_shouldThrowException_whenCourseDoesntExists() {
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFound.class, () -> courseService.publishCourse(1L));
        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("publish course should throw exception when type already PUBLISHED")
    void publishCourse_shouldThrowException_whenTypeIsPublished() {
        Course course = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Old name"
        );
        course.setStatus(CourseStatus.PUBLISHED);

        Mockito.when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        assertThrows(
                CourseAlreadyPublishedException.class,
                () -> courseService.publishCourse(course.getId())
        );

        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    @DisplayName("archive course should set type ARCHIVED if course exists")
    void archiveCourse_shouldSetTypeArchived() {
        Course course = CourseTestFactory
                .createTestCourse(
                        1L,
                        2L,
                        "Old name"
                );

        Mockito.when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));
        Mockito.when(courseRepository.save(any(Course.class)))
                .thenAnswer(i -> i.getArgument(0));

        courseService.archiveCourse(1L);

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(courseRepository).save(captor.capture());

        Course savedCourse = captor.getValue();
        Assertions.assertEquals(CourseStatus.ARCHIVED, savedCourse.getStatus());
    }

    @Test
    @DisplayName("archive course should throw exception when course doesn't exists")
    void archiveCourse_shouldThrowException_whenCourseDoesntExists() {
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFound.class, () -> courseService.archiveCourse(1L));
        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("archive course should throw exception when type already ARCHIVED")
    void archiveCourse_shouldThrowException_whenTypeIsARCHIVED() {
        Course course = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Old name"
        );
        course.setStatus(CourseStatus.ARCHIVED);

        Mockito.when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        assertThrows(
                CourseAlreadyArchivedException.class,
                () -> courseService.archiveCourse(course.getId())
        );

        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("delete course should delete if exists")
    void deleteCourse_whenCourseExists() {
        Long courseId = 1L;

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);
        courseService.deleteCourse(courseId);

        Mockito.verify(courseRepository).deleteById(courseId);
    }

    @Test
    @DisplayName("delete course should throw exception when course doesn't exists")
    void deleteCourse_shouldThrowException_whenCourseDoesntExists() {
        Long courseId = 1L;

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(false);

        assertThrows(CourseNotFound.class, () -> courseService.deleteCourse(courseId));

        Mockito.verify(courseRepository, Mockito.never()).deleteById(courseId);
    }


    @Test
    @DisplayName("get all courses returns mapped page")
    void getAllCourses_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course One"
        );
        Course course2 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course Two"
        );
        Course course3 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course Three"
        );

        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2, course3));

        Mockito.when(courseRepository.findAll(pageable)).thenReturn(coursePage);
        Mockito.when(courseMapper.toShortResponse(course1)).thenReturn(new CourseShortResponse(
                1L,
                "Course One",
                BigDecimal.TEN,
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
                ));
        Mockito.when(courseMapper.toShortResponse(course2)).thenReturn(new CourseShortResponse(
                2L,
                "Course Two",
                BigDecimal.TEN,
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
        ));
        Mockito.when(courseMapper.toShortResponse(course3)).thenReturn(new CourseShortResponse(
                3L,
                "Course Three",
                BigDecimal.TEN,
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
        ));

        Page<CourseShortResponse> result = courseService.getAllCourses(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getContent().size());
        Assertions.assertEquals("Course One", result.getContent().get(0).name());
        Assertions.assertEquals("Course Two", result.getContent().get(1).name());
        Assertions.assertEquals("Course Three", result.getContent().get(2).name());

        Mockito.verify(courseRepository).findAll(pageable);
        Mockito.verify(courseMapper).toShortResponse(course1);
        Mockito.verify(courseMapper).toShortResponse(course2);
        Mockito.verify(courseMapper).toShortResponse(course3);
    }


    @Test
    @DisplayName("get all courses by TeacherID returns mapped pages")
    void getCourseByTeacher_returnMappedPages() {
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course One"
        );
        Course course2 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course Two"
        );
        Course course3 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course Three"
        );

        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2, course3));
        Long teacherId = 2L;
        Mockito.when(courseRepository.findByTeacherId(teacherId , pageable)).thenReturn(coursePage);

        Mockito.when(courseMapper.toShortResponse(course1)).thenReturn(new CourseShortResponse(
                1L,
                "Course One",
                BigDecimal.TEN,
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
        ));
        Mockito.when(courseMapper.toShortResponse(course2)).thenReturn(new CourseShortResponse(
                2L,
                "Course Two",
                BigDecimal.TEN,
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
        ));
        Mockito.when(courseMapper.toShortResponse(course3)).thenReturn(new CourseShortResponse(
                3L,
                "Course Three",
                BigDecimal.TEN,
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
        ));

        Page<CourseShortResponse> result = courseService.getCourseByTeacher(teacherId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getContent().size());

        Mockito.verify(courseRepository).findByTeacherId(teacherId, pageable);
        Mockito.verify(courseMapper).toShortResponse(course1);
        Mockito.verify(courseMapper).toShortResponse(course2);
        Mockito.verify(courseMapper).toShortResponse(course3);
    }


    @Test
    @DisplayName("search course should return mapped pages of courses")
    void searchCourse_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course One"
        );
        Course course2 = CourseTestFactory.createTestCourse(
                2L,
                2L,
                "Course Two"
        );

        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2));

        CourseSearchFilter filter = new CourseSearchFilter(
                "Java",
                CourseAccessType.PAID,
                5L,
                BigDecimal.ZERO,
                BigDecimal.TEN
        );

        Mockito.when(courseRepository.searchCourses(
                filter.keyword(),
                filter.type(),
                filter.teacherId(),
                filter.priceFrom(),
                filter.priceTo(),
                pageable
        )).thenReturn(coursePage);

        Mockito.when(courseMapper.toShortResponse(course1))
                .thenReturn(new CourseShortResponse(1L, "Course One", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.PUBLISHED));
        Mockito.when(courseMapper.toShortResponse(course2))
                .thenReturn(new CourseShortResponse(2L, "Course Two", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.PUBLISHED));

        Page<CourseShortResponse> result = courseService.searchCourse(filter, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Course One", result.getContent().get(0).name());
        Assertions.assertEquals("Course Two", result.getContent().get(1).name());

        Mockito.verify(courseRepository).searchCourses(
                filter.keyword(),
                filter.type(),
                filter.teacherId(),
                filter.priceFrom(),
                filter.priceTo(),
                pageable
        );
        Mockito.verify(courseMapper).toShortResponse(course1);
        Mockito.verify(courseMapper).toShortResponse(course2);
    }


    @Test
    @DisplayName("get PUBLISHED course should return mapped pages with PUBLISHED course")
    void getPublishedCourse_shouldReturnMappedPages() {
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course One"
        );
        Course course2 = CourseTestFactory.createTestCourse(
                2L,
                2L,
                "Course Two"
        );

        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2));

        Mockito.when(courseRepository.findByStatus(CourseStatus.PUBLISHED, pageable))
                .thenReturn(coursePage);
        Mockito.when(courseMapper.toShortResponse(course1))
                .thenReturn(new CourseShortResponse(1L, "Course One", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.PUBLISHED));
        Mockito.when(courseMapper.toShortResponse(course2))
                .thenReturn(new CourseShortResponse(2L, "Course Two", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.PUBLISHED));

        Page<CourseShortResponse> result = courseService.getPublishedCourse(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Course One", result.getContent().get(0).name());
        Assertions.assertEquals("Course Two", result.getContent().get(1).name());

        Mockito.verify(courseRepository).findByStatus(CourseStatus.PUBLISHED, pageable);
    }


    @Test
    @DisplayName("get PUBLISHED course should return mapped pages with PUBLISHED course")
    void getArchivedCourse_returnMappedPages() {
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course One"
        );
        course1.setStatus(CourseStatus.ARCHIVED);

        Course course2 = CourseTestFactory.createTestCourse(
                2L,
                2L,
                "Course Two"
        );
        course2.setStatus(CourseStatus.ARCHIVED);

        Course course3 = CourseTestFactory.createTestCourse(
                2L,
                2L,
                "Course Three"
        );
        course3.setStatus(CourseStatus.PUBLISHED);

        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2));

        Mockito.when(courseRepository.findByStatus(CourseStatus.ARCHIVED, pageable))
                .thenReturn(coursePage);
        Mockito.when(courseMapper.toShortResponse(course1))
                .thenReturn(new CourseShortResponse(1L, "Course One", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.ARCHIVED));
        Mockito.when(courseMapper.toShortResponse(course2))
                .thenReturn(new CourseShortResponse(2L, "Course Two", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.ARCHIVED));


        Page<CourseShortResponse> result = courseService.getArchivedCourse(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Course One", result.getContent().get(0).name());
        Assertions.assertEquals("Course Two", result.getContent().get(1).name());

        Mockito.verify(courseRepository).findByStatus(CourseStatus.ARCHIVED, pageable);
    }

    @Test
    @DisplayName("get my course should return mapped pages of courses")
    void getMyCourse_shouldReturnMappedPages() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course One"
        );

        Course course2 = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Course Two"
        );

        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2));

        Mockito.when(courseRepository.findAllByUserId(userId, pageable)).thenReturn(coursePage);
        Mockito.when(courseMapper.toShortResponse(course1))
                .thenReturn(new CourseShortResponse(1L, "My Course 1", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.PUBLISHED));
        Mockito.when(courseMapper.toShortResponse(course2))
                .thenReturn(new CourseShortResponse(2L, "My Course 2", BigDecimal.TEN, CourseAccessType.PAID, 60, CourseStatus.PUBLISHED));

        Page<CourseShortResponse> result = courseService.getMyCourse(userId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("My Course 1", result.getContent().get(0).name());
        Assertions.assertEquals("My Course 2", result.getContent().get(1).name());

        Mockito.verify(courseRepository).findAllByUserId(userId, pageable);
        Mockito.verify(courseMapper).toShortResponse(course1);
        Mockito.verify(courseMapper).toShortResponse(course2);
    }

    @Test
    @DisplayName("count all courses should return correct value")
    void countAllCourses_returnsCorrectValue() {
        Mockito.when(courseRepository.count()).thenReturn(42L);

        long result = courseService.countAllCourses();

        Assertions.assertEquals(42L, result);
        Mockito.verify(courseRepository).count();
    }


    @Test
    @DisplayName("get course by id should return course when exists")
    void getCourseById_courseExists_returnsMappedResponse() {
        Course course = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Test Course"
        );

        CourseResponse mappedResponse = new CourseResponse(
                1L,
                2L,
                "Test Course",
                "Test Description",
                BigDecimal.TEN,
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID,
                60,
                null,
                null
        );

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(courseMapper.toResponse(course)).thenReturn(mappedResponse);

        CourseResponse result = courseService.getCourseById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Course", result.name());
        Mockito.verify(courseRepository).findById(1L);
        Mockito.verify(courseMapper).toResponse(course);
    }

    @Test
    @DisplayName("get course by id should throw exception when course doesn't exists")
    void getCourseById_courseNotFound_throwsException() {
        Long courseId = 2L;
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFound.class, () -> courseService.getCourseById(courseId));
        Mockito.verify(courseRepository).findById(courseId);
    }

    @Test
    @DisplayName("get course by id should throw exception when course status is archived")
    void getCourseById_courseArchived_throwsException() {
        Long courseId = 3L;

        Course course = CourseTestFactory.createTestCourse(
                1L,
                2L,
                "Test Course"
        );
        course.setStatus(CourseStatus.ARCHIVED);

        Mockito.when(courseRepository.findById(3L)).thenReturn(Optional.of(course));

        Assertions.assertThrows(CourseNotFound.class, () -> courseService.getCourseById(courseId));
        Mockito.verify(courseRepository).findById(courseId);
    }
}
