package com.example.mercado.courses.course.courseRequirement.service.interfaces;

import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;

import java.util.List;

public interface CoursePublicRequirementService {

    RequirementResponse getCourseRequirement(Long requirementId, Long courseId);

    List<RequirementResponse> getAllByCourseId(Long courseId);

}
