package com.example.mercado.courses.course.courseRequirement.service.interfaces;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;

import java.util.List;

public interface CourseRequirementService {

    RequirementResponse addCourseRequirement(Long courseId, AddRequirementRequest request);
    RequirementResponse updateCourseRequirement(Long requirementId, Long courseId, UpdateRequirementRequest request);
    RequirementResponse getCourseRequirement(Long requirementId, Long courseId);

    void deleteCourseRequirement(Long requirementId, Long courseId);

    List<RequirementResponse> getAllByCourseId(Long courseId);

    RequirementResponse updatePosition(Long requirementId, Long courseId, ReorderRequirementRequest request);
}
