package com.example.mercado.courses.course.controller;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.service.interfaces.CourseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class CourseAdminController {

    private final CourseCommandService service;

    @PostMapping
    public CourseDetailsResponse createCourse(
            @RequestBody CreateCourseRequest request
            ) {
        return service.createCourse(request);
    }

    @PatchMapping("/{courseId}")
    public CourseDetailsResponse updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseRequest request
            ) {
        return service.updateCourse(courseId, request);
    }

    @PutMapping("/{courseId}/status")
    public void changeCourseStatus(
            @PathVariable Long courseId,
            @RequestBody ChangeStatusRequest request
    ) {
        service.changeCourseStatus(courseId, request);
    }

    @PutMapping("/{courseId}/level")
    public void changeCourseLevel(
            @PathVariable Long courseId,
            @RequestBody ChangeLevelRequest request
    ) {
        service.changeCourseLevel(courseId, request);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(
            @PathVariable Long courseId
    ) {
        service.deleteCourse(courseId);
    }

}
