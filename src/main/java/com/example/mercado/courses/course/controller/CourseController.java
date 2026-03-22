package com.example.mercado.courses.course.controller;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.service.interfaces.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(
            @RequestBody CreateCourseRequest dto
    ) {
        return courseService.createCourse(dto);
    }

    @PatchMapping("/{courseId}")
    public CourseResponse updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseRequest request
    ){
        return courseService.updateCourse(courseId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(
            @PathVariable Long courseId
    ){
        courseService.deleteCourse(courseId);
    }

    @GetMapping
    public Page<CourseShortResponse> getAllCourses(
        @PageableDefault(
                sort = "createdAt",
                direction = Sort.Direction.DESC
        )
        Pageable pageable
    )
    {
        return courseService.getAllCourses(pageable);
    }

    @GetMapping("/{courseId}")
    public CourseResponse getCourse(
            @PathVariable Long courseId
    ){
        return courseService.getCourseById(courseId);
    }

    @GetMapping("/count")
    public long countAllCourses()
    {
        return courseService.countAllCourses();
    }

    @GetMapping("/popular")
    public List<CourseShortResponse> getPopularCourse() {
        return courseService.getPopularCourses();
    }

    @GetMapping("/my/{userId}")
    public Page<CourseShortResponse> getMyCourse(
            @PathVariable Long userId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return courseService.getMyCourse(userId, pageable);
    }

    @GetMapping("/status/archived")
    public Page<CourseShortResponse> getArchivedCourse(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return courseService.getArchivedCourse(pageable);
    }

    @GetMapping("/status/published")
    public Page<CourseShortResponse> getPublishedCourse(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return courseService.getPublishedCourse(pageable);
    }

    @GetMapping("/search")
    public Page<CourseShortResponse> searchCourse(
            @RequestBody CourseSearchFilter filter,
            @PageableDefault(sort = "createdAt") Pageable pageable
    ) {
        return courseService.searchCourse(filter, pageable);
    }

    @GetMapping("/teacher/{teacherId}")
    public Page<CourseShortResponse> getCoursesByTeacher(
            @PathVariable Long teacherId,
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return courseService.getCourseByTeacher(teacherId, pageable);
    }
}
