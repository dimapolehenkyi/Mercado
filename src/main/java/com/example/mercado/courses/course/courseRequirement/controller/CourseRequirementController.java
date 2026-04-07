package com.example.mercado.courses.course.courseRequirement.controller;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.service.interfaces.CourseRequirementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/requirements")
@RequiredArgsConstructor
public class CourseRequirementController {

    private final CourseRequirementService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RequirementResponse addCourseRequirement(
            @PathVariable Long courseId,
            @RequestBody @Valid AddRequirementRequest request
    ) {
        return service.addCourseRequirement(courseId, request);
    }

    @PatchMapping("/{requirementId}")
    public RequirementResponse updateCourseRequirement(
            @PathVariable Long courseId,
            @PathVariable Long requirementId,
            @RequestBody @Valid UpdateRequirementRequest request
    ) {
        return service.updateCourseRequirement(courseId, requirementId, request);
    }

    @GetMapping("/{requirementId}")
    public RequirementResponse getCourseRequirement(
            @PathVariable Long courseId,
            @PathVariable Long requirementId
    ) {
        return service.getCourseRequirement(courseId, requirementId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{requirementId}")
    public void deleteCourseRequirement(
            @PathVariable Long courseId,
            @PathVariable Long requirementId
    ) {
        service.deleteCourseRequirement(courseId, requirementId);
    }

    @GetMapping
    public List<RequirementResponse> getAllByCourseId(
            @PathVariable Long courseId
    ) {
        return service.getAllByCourseId(courseId);
    }

    @PatchMapping("/{requirementId}/position")
    public RequirementResponse updatePosition(
            @PathVariable Long courseId,
            @PathVariable Long requirementId,
            @RequestBody @Valid ReorderRequirementRequest request
    ) {
        return service.updatePosition(courseId, requirementId, request);
    }

}
