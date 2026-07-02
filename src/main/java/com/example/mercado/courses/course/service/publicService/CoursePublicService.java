package com.example.mercado.courses.course.service.publicService;

import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseSearchFilter;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoursePublicService {

    Page<CourseShortResponse> getAllCourses(Pageable pageable);
    Page<CourseShortResponse> getCoursesByTeacherId(Long teacherId, Pageable pageable);
    Page<CourseShortResponse> searchCourse(CourseSearchFilter filter, Pageable pageable);
    Page<CourseShortResponse> getMyCourse(Long userId, Pageable pageable);

    Page<CourseShortResponse> getPopularCourses(Pageable pageable);

    CourseDetailsResponse getActiveCourseById(Long courseId);

    long countAllCourses();
    long countTeacherCoursesById(Long teacherId);

}
