package com.example.mercado.courses.course.controller;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.service.interfaces.CourseAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@Validated
@RequiredArgsConstructor
public class CourseAdminController {

    private final CourseAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDetailsResponse createCourse(
            @Valid @RequestBody CreateCourseRequest request
            ) {
        return service.createCourse(request);
    }

    @PatchMapping("/{courseId}")
    public CourseDetailsResponse updateCourse(
            @PathVariable @Positive Long courseId,
            @Valid @RequestBody UpdateCourseRequest request
            ) {
        return service.updateCourse(courseId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{courseId}/status")
    public void changeCourseStatus(
            @PathVariable @Positive Long courseId,
            @Valid @RequestBody ChangeStatusRequest request
    ) {
        service.changeCourseStatus(courseId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{courseId}/level")
    public void changeCourseLevel(
            @PathVariable @Positive Long courseId,
            @Valid @RequestBody ChangeLevelRequest request
    ) {
        service.changeCourseLevel(courseId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(
            @PathVariable @Positive Long courseId
    ) {
        service.deleteCourse(courseId);
    }

}
