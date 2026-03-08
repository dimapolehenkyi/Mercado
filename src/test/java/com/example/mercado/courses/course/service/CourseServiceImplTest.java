package com.example.mercado.courses.course.service;

import com.example.mercado.courses.course.dto.CourseResponse;
import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.exception.CourseAlreadyArchivedException;
import com.example.mercado.courses.course.exception.CourseAlreadyExistsException;
import com.example.mercado.courses.course.exception.CourseAlreadyPublishedException;
import com.example.mercado.courses.course.exception.CourseNotFound;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.testutils.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;


    @Test
    @DisplayName("create course should return CourseResponse")
    void createCourse_shouldReturnCourseResponse() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequest();

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
    @DisplayName("create course should throw exception when course exists")
    void createCourse_shouldThrowException_whenCourseExists() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequest();

        Course course = new Course();
        course.setName(request.name());

        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(true);
        Mockito.verify(courseRepository, Mockito.never()).save(any(Course.class));

        Assertions.assertThrows(CourseAlreadyExistsException.class, () -> courseService.createCourse(request));
    }


    @Test
    @DisplayName("create course should set price ZERO when status FREE")
    void createCourse_shouldSetPriceZero_whenStatusFree() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequestFree();

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
    @DisplayName("update course should return course response with updated data")
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
    @DisplayName("update course should throw exception when course exists")
    void updateCourse_shouldThrowException_whenCourseExists() {
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();

        Course course = CourseTestFactory.createTestCourse(1L, 2L, "Old name");

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(courseRepository.existsByName(request.name())).thenReturn(true);
        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());

        Assertions.assertThrows(CourseAlreadyExistsException.class, () -> courseService.updateCourse(1L, request));
    }

    @Test
    @DisplayName("update course should set price zero when status is free")
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
    @DisplayName("publish course should set type PUBLISHED if course exists")
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
    @DisplayName("publish course should throw exception when course doesn't exists")
    void publishCourse_shouldThrowException_whenCourseDoesntExists() {
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(CourseNotFound.class, () -> courseService.publishCourse(1L));
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

        Assertions.assertThrows(
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

        Assertions.assertThrows(CourseNotFound.class, () -> courseService.archiveCourse(1L));
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

        Assertions.assertThrows(
                CourseAlreadyArchivedException.class,
                () -> courseService.archiveCourse(course.getId())
        );

        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }
}
