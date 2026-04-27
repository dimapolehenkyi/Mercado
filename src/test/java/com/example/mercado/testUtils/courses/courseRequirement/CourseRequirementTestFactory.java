package com.example.mercado.testUtils.courses.courseRequirement;

import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;

public class CourseRequirementTestFactory {

    public static CourseRequirement.CourseRequirementBuilder createDefaultCourseRequirement() {
        return CourseRequirement.builder()
                .courseId(1L)
                .text("Test text")
                .position(1);
    }

}
