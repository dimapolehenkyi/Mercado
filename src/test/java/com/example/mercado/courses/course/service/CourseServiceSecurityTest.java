package com.example.mercado.courses.course.service;

import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.interfaces.CourseService;
import com.example.mercado.courses.testutils.course.CourseTestFactory;
import com.example.mercado.mail.MailService;
import com.example.mercado.mail.test.MailConfigTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(MailConfigTest.class)
@ActiveProfiles("test")
public class CourseServiceSecurityTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @MockitoBean
    private MailService mailService;

    @Test
    @DisplayName("create course as ADMIN allows access")
    @WithMockUser(authorities = "ADMIN")
    void createCourse_asAdmin_allowsAccess() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequest();
        Assertions.assertDoesNotThrow(() -> courseService.createCourse(request));
    }

    @Test
    @DisplayName("create course as STUDENT denies access")
    @WithMockUser(authorities = "STUDENT")
    void createCourse_asStudent_deniesAccess() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequest();
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.createCourse(request)
        );
    }

    @Test
    @DisplayName("create course as COMMON_USER denies access")
    @WithMockUser(authorities = "COMMON_USER")
    void createCourse_asCommonUser_deniesAccess() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequest();
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.createCourse(request)
        );
    }

    @Test
    @DisplayName("create course as TEACHER denies access")
    @WithMockUser(authorities = "TEACHER")
    void createCourse_asTeacher_deniesAccess() {
        CreateCourseRequest request = CourseTestFactory.createTestCreateCourseRequest();
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.createCourse(request)
        );
    }

    @Test
    @DisplayName("update course as ADMIN allows access")
    @WithMockUser(authorities = "ADMIN")
    void updateCourse_asAdmin_allowsAccess() {
        Course course  = CourseTestFactory.createTestCourse(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();
        Assertions.assertDoesNotThrow(() -> courseService.updateCourse(savedCourse.getId(), request));
    }


    @Test
    @DisplayName("update course as STUDENT denies access")
    @WithMockUser(authorities = "STUDENT")
    void updateCourse_asStudent_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourse(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.updateCourse(savedCourse.getId(), request)
        );
    }


    @Test
    @DisplayName("update course as COMMON_USER denies access")
    @WithMockUser(authorities = "TEACHER")
    void updateCourse_asTeacher_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourse(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.updateCourse(savedCourse.getId(), request)
        );
    }


    @Test
    @DisplayName("update course as COMMON_USER denies access")
    @WithMockUser(authorities = "COMMON_USER")
    void updateCourse_asCommonUser_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourse(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.updateCourse(savedCourse.getId(), request)
        );
    }


    @Test
    @DisplayName("publish course as ADMIN allows access")
    @WithMockUser(authorities = "ADMIN")
    void publishCourse_asAdmin_allowsAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertDoesNotThrow(() -> courseService.publishCourse(savedCourse.getId()));
    }

    @Test
    @DisplayName("publish course as TEACHER denies access")
    @WithMockUser(authorities = "TEACHER")
    void publishCourse_asTeacher_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.publishCourse(savedCourse.getId())
        );
    }

    @Test
    @DisplayName("publish course as STUDENT denies access")
    @WithMockUser(authorities = "STUDENT")
    void publishCourse_asStudent_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.publishCourse(savedCourse.getId())
        );
    }

    @Test
    @DisplayName("publish course as COMMON_USER denies access")
    @WithMockUser(authorities = "COMMON_USER")
    void publishCourse_asCommonUser_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.publishCourse(savedCourse.getId())
        );
    }



    @Test
    @DisplayName("archive course as ADMIN denies access")
    @WithMockUser(authorities = "ADMIN")
    void archiveCourse_asAdmin_AllowsAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertDoesNotThrow(() -> courseService.archiveCourse(savedCourse.getId()));
    }


    @Test
    @DisplayName("archive course as STUDENT denies access")
    @WithMockUser(authorities = "STUDENT")
    void archiveCourse_asStudent_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.archiveCourse(savedCourse.getId())
        );
    }


    @Test
    @DisplayName("archive course as TEACHER denies access")
    @WithMockUser(authorities = "TEACHER")
    void archiveCourse_asTeacher_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.archiveCourse(savedCourse.getId())
        );
    }


    @Test
    @DisplayName("archive course as COMMON_USER denies access")
    @WithMockUser(authorities = "COMMON_USER")
    void archiveCourse_asCommonUser_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.archiveCourse(savedCourse.getId())
        );
    }


    @Test
    @DisplayName("delete course as ADMIN allows access")
    @WithMockUser(authorities = "ADMIN")
    void deleteCourse_asStudent_AllowsAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertDoesNotThrow(() -> courseService.deleteCourse(savedCourse.getId()));
    }

    @Test
    @DisplayName("delete course as STUDENT denies access")
    @WithMockUser(authorities = "STUDENT")
    void deleteCourse_asStudent_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.deleteCourse(savedCourse.getId())
        );
    }

    @Test
    @DisplayName("delete course as TEACHER denies access")
    @WithMockUser(authorities = "TEACHER")
    void deleteCourse_asTeacher_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.deleteCourse(savedCourse.getId())
        );
    }

    @Test
    @DisplayName("delete course as COMMON_USER denies access")
    @WithMockUser(authorities = "COMMON_USER")
    void deleteCourse_asCommonUser_deniesAccess() {
        Course course  = CourseTestFactory.createTestCourseDraft(
                1L, 2L, "Test Course"
        );
        Course savedCourse = courseRepository.save(course);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.deleteCourse(savedCourse.getId())
        );
    }

    @Test
    @DisplayName("get archived course as ADMIN allowed access")
    @WithMockUser(authorities = "ADMIN")
    void getArchivedCourse_asAdmin_AllowsAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertDoesNotThrow(() -> courseService.getArchivedCourse(pageable));
    }

    @Test
    @DisplayName("get archived course as COMMON_USER denies access")
    @WithMockUser(authorities = "COMMON_USER")
    void getArchivedCourse_asCommonUser_DeniesAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.getArchivedCourse(pageable)
        );
    }

    @Test
    @DisplayName("get archived course as STUDENT denies access")
    @WithMockUser(authorities = "STUDENT")
    void getArchivedCourse_asStudent_DeniesAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.getArchivedCourse(pageable)
        );
    }

    @Test
    @DisplayName("get archived course as TEACHER denies access")
    @WithMockUser(authorities = "TEACHER")
    void getArchivedCourse_asTeacher_DeniesAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.getArchivedCourse(pageable)
        );
    }

    @Test
    @DisplayName("get my course as STUDENT allowed access")
    @WithMockUser(authorities = "STUDENT")
    void getMyCourse_asStudent_allowsAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertDoesNotThrow(() ->
                courseService.getMyCourse(1L, pageable)
        );
    }

    @Test
    @DisplayName("get my course as TEACHER allowed access")
    @WithMockUser(authorities = "TEACHER")
    void getMyCourse_asTeacher_allowsAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertDoesNotThrow(() ->
                courseService.getMyCourse(1L, pageable)
        );
    }

    @Test
    @DisplayName("get my course as COMMON_USER allowed access")
    @WithMockUser(authorities = "COMMON_USER")
    void getMyCourse_asCommonUser_allowsAccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertThrows(
                AuthorizationDeniedException.class,
                () -> courseService.getMyCourse(1L, pageable)
        );
    }

}
