package com.example.mercado.courses.course.courseLearningPoint.controller.publicController;


import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CoursePublicLearningPointService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses/{courseId}/learning-points")
@Validated
@RequiredArgsConstructor
public class CoursePublicLearningPointController {


    private final CoursePublicLearningPointService service;


    @GetMapping("/{pointId}")
    public LearningPointResponse getLearningPoint(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long pointId
    ) {
        return service.getCourseLearningPoint(pointId, courseId);
    }

    @GetMapping
    public List<LearningPointResponse> getAllByCourseId(
            @PathVariable @Positive Long courseId
    ) {
        return service.getCourseLearningPoints(courseId);
    }

}
