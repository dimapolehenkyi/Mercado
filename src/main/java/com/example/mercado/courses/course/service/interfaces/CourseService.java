package com.example.mercado.courses.course.service.interfaces;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.enums.CourseAccessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request);
    CourseResponse updateCourse(Long courseId, UpdateCourseRequest request);

    void publishCourse(Long courseId);
    void archiveCourse(Long courseId);
    void deleteCourse(Long courseId);

    Page<CourseShortResponse> getAllCourses(Pageable pageable);
    Page<CourseShortResponse> getCourseByTeacher(Long teacherId, Pageable pageable);
    Page<CourseShortResponse> searchCourse(CourseSearchFilter filter, Pageable pageable);
    Page<CourseShortResponse> getPublishedCourse(Pageable pageable);
    Page<CourseShortResponse> getArchivedCourse(Pageable pageable);
    Page<CourseShortResponse> getMyCourse(Long userId, Pageable pageable);

    List<CourseShortResponse> getPopularCourses();

    long countAllCourses();

    CourseResponse getCourseById(Long courseId);
}
