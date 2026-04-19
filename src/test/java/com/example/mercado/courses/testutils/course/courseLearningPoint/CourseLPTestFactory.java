package com.example.mercado.courses.testutils.course.courseLearningPoint;

import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;

public class CourseLPTestFactory {

    public static CourseLearningPoint.CourseLearningPointBuilder createDefaultCourseLP() {
        return CourseLearningPoint.builder()
                .courseId(1L)
                .text("Test text")
                .position(1);
    }

}
