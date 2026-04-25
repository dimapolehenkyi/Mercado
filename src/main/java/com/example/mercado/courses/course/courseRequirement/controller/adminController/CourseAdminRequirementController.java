package com.example.mercado.courses.course.courseRequirement.controller.adminController;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.service.interfaces.CourseAdminRequirementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/admin/requirements")
@Validated
@RequiredArgsConstructor
public class CourseAdminRequirementController {


    private final CourseAdminRequirementService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RequirementResponse createCourseRequirement(
            @PathVariable @Positive Long courseId,
            @RequestBody @Valid AddRequirementRequest request
    ) {
        return service.createCourseRequirement(courseId, request);
    }

    @PutMapping("/{requirementId}")
    public RequirementResponse updateCourseRequirement(
            @PathVariable @Positive Long requirementId,
            @PathVariable @Positive Long courseId,
            @RequestBody @Valid UpdateRequirementRequest request
    ) {
        return service.updateCourseRequirement(requirementId, courseId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{requirementId}")
    public void deleteCourseRequirement(
            @PathVariable @Positive Long requirementId,
            @PathVariable @Positive Long courseId
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
