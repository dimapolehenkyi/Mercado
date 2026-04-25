package com.example.mercado.testUtils.courses.courseLearningPoint;

import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;

public class CourseLPTestData {

    public static LearningPointResponse mapToLearningPointResponse(CourseLearningPoint point) {
        return new LearningPointResponse(
                point.getId(),
                point.getCourseId(),
                point.getText(),
                point.getPosition()
        );
    }

}
