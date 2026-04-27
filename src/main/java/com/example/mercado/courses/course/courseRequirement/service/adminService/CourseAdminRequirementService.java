package com.example.mercado.courses.course.courseRequirement.service.adminService;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;

public interface CourseAdminRequirementService {

    RequirementResponse createCourseRequirement(Long courseId, AddRequirementRequest request);
    RequirementResponse updateCourseRequirement(Long requirementId, Long courseId, UpdateRequirementRequest request);

    void deleteCourseRequirement(Long requirementId, Long courseId);

    RequirementResponse updatePosition(Long requirementId, Long courseId, ReorderRequirementRequest request);

}
