package com.example.mercado.courses.course.courseLearningPoint.service.interfaces;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;

import java.util.List;

public interface CourseLearningPointService {

    LearningPointResponse createCourseLearningPoint(Long courseId, AddLearningPointRequest request);
    LearningPointResponse updateCourseLearningPoint(Long pointId, Long courseId, UpdateLearningPointRequest request);
    LearningPointResponse getCourseLearningPoint(Long pointId, Long courseId);

    void deleteCourseLearningPoint(Long pointId, Long courseId);

    List<LearningPointResponse> getAllLearningPointsByCourseId(Long courseId);

    LearningPointResponse updatePositionCourseLearningPoint(Long pointId, Long courseId, ReorderLearningPointRequest request);
}
