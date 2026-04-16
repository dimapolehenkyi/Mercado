package com.example.mercado.courses.course.courseLearningPoint.controller;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("courses/{courseId}/learning-points")
@RequiredArgsConstructor
public class CourseAdminLearningPointController {


    private final CourseAdminLearningPointService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public LearningPointResponse addCourseRequirement(
            @PathVariable Long courseId,
            @RequestBody @Valid AddLearningPointRequest request
    ) {
        return service.createCourseLearningPoint(courseId, request);
    }

    @PatchMapping("/{pointId}")
    public LearningPointResponse updateCourseRequirement(
            @PathVariable Long courseId,
            @PathVariable Long pointId,
            @RequestBody @Valid UpdateLearningPointRequest request
    ) {
        return service.updateCourseLearningPoint(courseId, pointId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{pointId}")
    public void deleteCourseRequirement(
            @PathVariable Long courseId,
            @PathVariable Long pointId
    ) {
        service.deleteCourseLearningPoint(courseId, pointId);
    }

    @PatchMapping("/{pointId}/position")
    public LearningPointResponse updatePosition(
            @PathVariable Long courseId,
            @PathVariable Long pointId,
            @RequestBody @Valid ReorderLearningPointRequest request
    ) {
        return service.updatePositionCourseLearningPoint(courseId, pointId, request);
    }

}
