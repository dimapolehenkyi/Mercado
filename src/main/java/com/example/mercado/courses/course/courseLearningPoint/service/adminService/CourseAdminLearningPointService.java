package com.example.mercado.courses.course.courseLearningPoint.service.adminService;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;

public interface CourseAdminLearningPointService {

    LearningPointResponse createCourseLearningPoint(Long courseId, AddLearningPointRequest request);
    LearningPointResponse updateCourseLearningPoint(Long pointId, Long courseId, UpdateLearningPointRequest request);

    void deleteCourseLearningPoint(Long pointId, Long courseId);

    LearningPointResponse updatePositionCourseLearningPoint(Long pointId, Long courseId, ReorderLearningPointRequest request);
}
