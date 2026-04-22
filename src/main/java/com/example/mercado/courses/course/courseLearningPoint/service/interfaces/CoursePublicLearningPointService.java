package com.example.mercado.courses.course.courseLearningPoint.service.interfaces;

import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;

import java.util.List;

public interface CoursePublicLearningPointService {

    LearningPointResponse getCourseLearningPoint(Long pointId, Long courseId);

    List<LearningPointResponse> getCourseLearningPoints(Long courseId);

}
