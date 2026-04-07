package com.example.mercado.courses.course.courseRequirement.mapper;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import org.springframework.stereotype.Component;

@Component
public class CourseRequirementMapper {

    public CourseRequirement toEntity(Long courseId, AddRequirementRequest request) {
        return CourseRequirement.builder()
                .courseId(courseId)
                .text(request.text())
                .build();
    }

    public void updateEntity(CourseRequirement courseRequirement, UpdateRequirementRequest request) {
        courseRequirement.setText(request.text());
    }

    public RequirementResponse toResponse(CourseRequirement courseRequirement) {
        return new RequirementResponse(
                courseRequirement.getId(),
                courseRequirement.getCourseId(),
                courseRequirement.getText(),
                courseRequirement.getPosition()
        );
    }

}
