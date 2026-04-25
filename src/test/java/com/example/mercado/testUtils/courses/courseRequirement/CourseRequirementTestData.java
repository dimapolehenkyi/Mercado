package com.example.mercado.testUtils.courses.courseRequirement;

import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;

public class CourseRequirementTestData {

    public static RequirementResponse mapToRequirementResponse(CourseRequirement requirement) {
        return new RequirementResponse(
                requirement.getId(),
                requirement.getCourseId(),
                requirement.getText(),
                requirement.getPosition()
        );
    }

}
