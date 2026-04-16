package com.example.mercado.courses.course.service.interfaces;

import com.example.mercado.courses.course.dto.*;

public interface CourseCommandService {

    CourseDetailsResponse createCourse(CreateCourseRequest request);
    CourseDetailsResponse updateCourse(Long courseId, UpdateCourseRequest request);

    void changeCourseStatus(Long courseId, ChangeStatusRequest request);
    void changeCourseLevel(Long courseId, ChangeLevelRequest request);
    void deleteCourse(Long courseId);

}
