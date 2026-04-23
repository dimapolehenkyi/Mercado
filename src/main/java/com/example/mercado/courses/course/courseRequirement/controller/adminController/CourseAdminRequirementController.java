package com.example.mercado.courses.course.courseRequirement.controller.adminController;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.service.interfaces.CourseAdminRequirementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/admin/requirements")
@RequiredArgsConstructor
public class CourseAdminRequirementController {


    private final CourseAdminRequirementService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RequirementResponse addCourseRequirement(
            @PathVariable Long courseId,
            @RequestBody @Valid AddRequirementRequest request
    ) {
        return service.createCourseRequirement(courseId, request);
    }

    @PutMapping("/{requirementId}")
    public RequirementResponse updateCourseRequirement(
            @PathVariable Long requirementId,
            @PathVariable Long courseId,
            @RequestBody @Valid UpdateRequirementRequest request
    ) {
        return service.updateCourseRequirement(requirementId, courseId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{requirementId}")
    public void deleteCourseRequirement(
            @PathVariable Long requirementId,
            @PathVariable Long courseId
    ) {
        service.deleteCourseRequirement(requirementId, courseId);
    }

    @PatchMapping("/{requirementId}/position")
    public RequirementResponse updatePosition(
            @PathVariable Long requirementId,
            @PathVariable Long courseId,
            @RequestBody @Valid ReorderRequirementRequest request
    ) {
        return service.updatePosition(requirementId, courseId, request);
    }

}
