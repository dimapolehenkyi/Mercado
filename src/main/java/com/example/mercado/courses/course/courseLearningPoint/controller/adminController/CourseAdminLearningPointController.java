package com.example.mercado.courses.course.courseLearningPoint.controller.adminController;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("courses/{courseId}/admin/learning-points")
@RequiredArgsConstructor
public class CourseAdminLearningPointController {


    private final CourseAdminLearningPointService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public LearningPointResponse addCourseLearningPoint(
            @PathVariable @Positive Long courseId,
            @RequestBody @Valid AddLearningPointRequest request
    ) {
        return service.createCourseLearningPoint(courseId, request);
    }

    @PutMapping("/{pointId}")
    public LearningPointResponse updateCourseLearningPoint(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long pointId,
            @RequestBody @Valid UpdateLearningPointRequest request
    ) {
        return service.updateCourseLearningPoint(courseId, pointId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{pointId}")
    public void deleteCourseLearningPoint(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long pointId
    ) {
        service.deleteCourseLearningPoint(courseId, pointId);
    }

    @PatchMapping("/{pointId}/position")
    public LearningPointResponse updatePosition(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long pointId,
            @RequestBody @Valid ReorderLearningPointRequest request
    ) {
        return service.updatePositionCourseLearningPoint(courseId, pointId, request);
    }

}
