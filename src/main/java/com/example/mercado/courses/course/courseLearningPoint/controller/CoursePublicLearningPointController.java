package com.example.mercado.courses.course.courseLearningPoint.controller;


import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CoursePublicLearningPointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses/{courseId}/learning-points")
@RequiredArgsConstructor
public class CoursePublicLearningPointController {


    private final CoursePublicLearningPointService service;

    @GetMapping("/{pointId}")
    public LearningPointResponse getCourseRequirement(
            @PathVariable Long courseId,
            @PathVariable Long pointId
    ) {
        return service.getCourseLearningPoint(courseId, pointId);
    }

    @GetMapping
    public List<LearningPointResponse> getAllByCourseId(
            @PathVariable Long courseId
    ) {
        return service.getCourseLearningPoints(courseId);
    }

}
