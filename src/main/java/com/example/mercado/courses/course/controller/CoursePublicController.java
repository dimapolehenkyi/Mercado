package com.example.mercado.courses.course.controller;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.service.interfaces.CourseQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Validated
public class CoursePublicController {

    private final CourseQueryService service;


    @GetMapping("/{courseId}")
    public CourseDetailsResponse getCourse(
            @PathVariable @Positive Long courseId
    ){
        return service.getActiveCourseById(courseId);
    }

    @GetMapping("/popular")
    public Page<CourseShortResponse> getPopularCourse(
            @PageableDefault(
                    sort = "studentCount",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return service.getPopularCourses(pageable);
    }

    @GetMapping("/users/{userId}/courses")
    public Page<CourseShortResponse> getMyCourse(
            @PathVariable @Positive Long userId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return service.getMyCourse(userId, pageable);
    }

    @GetMapping
    public Page<CourseShortResponse> getCoursesByStatus (
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable,
            @RequestParam (required = false) CourseStatus status
    ) {
        return service.getCoursesByStatus(status, pageable);
    }

    @GetMapping("/search")
    public Page<CourseShortResponse> searchCourse (
            @Valid CourseSearchFilter filter,
            @PageableDefault(sort = "createdAt") Pageable pageable
    ) {
        return service.searchCourse(filter, pageable);
    }

    @GetMapping("/teachers/{teacherId}/courses")
    public Page<CourseShortResponse> getCoursesByTeacherId(
            @PathVariable @Positive Long teacherId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return service.getCoursesByTeacherId(teacherId, pageable);
    }

    @GetMapping("/count")
    public long countAllCourses() {
        return service.countAllCourses();
    }

    @GetMapping("/teachers/{teacherId}/count")
    public long countTeacherCoursesById(
            @PathVariable @Positive Long teacherId
    ) {
        return service.countTeacherCoursesById(teacherId);
    }
}
