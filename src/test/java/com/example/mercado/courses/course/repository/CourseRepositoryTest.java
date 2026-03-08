package com.example.mercado.courses.course.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.testutils.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("find by name should return course when exists")
    void findByName_shouldReturnCourse_whenCourseExists() {
        Course course = new Course();
        course.setName("Test");
        course.setTeacherId(1L);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setDescription("Test description");
        course.setPrice(BigDecimal.ZERO);
        course.setType(CourseAccessType.FREE);
        course.setDurationInMinutes(0);

        Course savedCourse = courseRepository.save(course);

        Optional<Course> foundByName = courseRepository.findByName(savedCourse.getName());

        Assertions.assertTrue(foundByName.isPresent());
        Assertions.assertEquals(savedCourse.getName(), foundByName.get().getName());
    }

    @Test
    @DisplayName("find by name should return null when doesn't exists")
    void findByName_shouldReturnNull_whenCourseDoesntExists() {
        Optional<Course> foundByName = courseRepository.findByName("Not exists");
        Assertions.assertTrue(foundByName.isEmpty());
    }

    @Test
    @DisplayName("exists by name should return true when course exists")
    void existsByName_shouldReturnTrue_whenCourseExists() {
        Course course = new Course();
        course.setName("Test");
        course.setTeacherId(1L);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setDescription("Test description");
        course.setPrice(BigDecimal.ZERO);
        course.setType(CourseAccessType.FREE);
        course.setDurationInMinutes(0);

        Course savedCourse = courseRepository.save(course);

        boolean existsByName = courseRepository.existsByName(savedCourse.getName());

        Assertions.assertTrue(existsByName);
    }

    @Test
    @DisplayName("exists by name should return false when course doesnt exists")
    void existsByName_shouldReturnFalse_whenCourseDoesntExists() {
        boolean existsByName = courseRepository.existsByName("Not exists");
        Assertions.assertFalse(existsByName);
    }

    @Test
    @DisplayName("exists by id and status should return true when course exists")
    void existsByIdAndStatus_shouldReturnTrue_whenCourseExists() {
        Course course = new Course();
        course.setName("Test");
        course.setTeacherId(1L);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setDescription("Test description");
        course.setPrice(BigDecimal.ZERO);
        course.setType(CourseAccessType.FREE);
        course.setDurationInMinutes(0);

        Course savedCourse = courseRepository.save(course);

        boolean existsByIdAndStatus = courseRepository.existsByIdAndStatus(savedCourse.getId(), savedCourse.getStatus());

        Assertions.assertTrue(existsByIdAndStatus);
    }

    @Test
    @DisplayName("exists by id and status should return false when course doesn't exists")
    void existsByIdAndStatus_shouldReturnFalse_whenCourseDoesntExists() {
        Course course = new Course();
        course.setName("Test");
        course.setTeacherId(1L);
        course.setStatus(CourseStatus.ARCHIVED);
        course.setDescription("Test description");
        course.setPrice(BigDecimal.ZERO);
        course.setType(CourseAccessType.FREE);
        course.setDurationInMinutes(0);

        Course savedCourse = courseRepository.save(course);

        boolean existsByIdAndStatus = courseRepository.existsByIdAndStatus(savedCourse.getId(), CourseStatus.PUBLISHED);

        Assertions.assertFalse(existsByIdAndStatus);
    }

    @Test
    @DisplayName("count courses by teacherId should return correct count when teacherId exists")
    void countByTeacherId_shouldReturnCorrectCount_whenTeacherIdExists() {
        courseRepository.save(CourseTestFactory.createTestCourse(1L, 2L, "Course1"));
        courseRepository.save(CourseTestFactory.createTestCourse(1L, 2L, "Course2"));
        courseRepository.save(CourseTestFactory.createTestCourse(2L, 2L, "Course3"));

        long count = courseRepository.countByTeacherId(2L);

        Assertions.assertEquals(3, count);
    }

    @Test
    @DisplayName("count courses by userId should return correct count when userId exists")
    void findAllByUserId_shouldReturnCorrectPage_whenUserExists() {
        courseRepository.save(CourseTestFactory.createTestCourse(1L, 2L, "Course1"));
        courseRepository.save(CourseTestFactory.createTestCourse(1L, 2L, "Course2"));
        courseRepository.save(CourseTestFactory.createTestCourse(2L, 2L, "Course3"));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> result = courseRepository.findAllByUserId(1L, pageable);

        Assertions.assertEquals(2, result.getTotalElements());
    }


    @Test
    @DisplayName("find All by status PUBLISHED should return pages when courses exists")
    void findByStatusPublished_shouldReturnPages_whenCoursesExists() {
        courseRepository.save(CourseTestFactory.createTestCourse(1L, 2L, "Course1"));
        courseRepository.save(CourseTestFactory.createTestCourse(2L, 2L, "Course2"));
        courseRepository.save(CourseTestFactory.createTestCourse(3L, 2L, "Course3"));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Course> result = courseRepository.findByStatus(CourseStatus.PUBLISHED, pageable);

        Assertions.assertEquals(3, result.getTotalElements());
    }


    @Test
    @DisplayName("find by teacherId should return pages when teacherId exists")
    void findByTeacherId_shouldReturnCorrectPage_whenTeacherIdExists() {
        courseRepository.save(CourseTestFactory.createTestCourse(1L, 2L, "Course1"));
        courseRepository.save(CourseTestFactory.createTestCourse(2L, 2L, "Course2"));
        courseRepository.save(CourseTestFactory.createTestCourse(3L, 2L, "Course3"));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Course> result = courseRepository.findByTeacherId(2L, pageable);

        Assertions.assertEquals(3, result.getTotalElements());
    }
}
