package com.example.mercado.courses.assignment.controller;

import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;
import com.example.mercado.courses.assignment.service.interfaces.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons/{lessonId}/assignments")
public class AssignmentController {

    private final AssignmentService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AssignmentResponse createAssignment(
            @PathVariable Long lessonId,
            @RequestBody CreateAssignmentRequest request
            ) {
        return service.createAssignment(lessonId, request);
    }

    @PatchMapping("/{assignmentId}")
    public AssignmentResponse updateAssignment(
            @PathVariable Long lessonId,
            @PathVariable Long assignmentId,
            @RequestBody UpdateAssignmentRequest request
    ) {
        return service.updateAssignment(lessonId, assignmentId, request);
    }

    @GetMapping("/{assignmentId}")
    public AssignmentResponse getAssignment(
            @PathVariable Long lessonId,
            @PathVariable Long assignmentId
    ) {
        return service.getAssignment(lessonId, assignmentId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{assignmentId}")
    public void deleteAssignment(
            @PathVariable Long lessonId,
            @PathVariable Long assignmentId
    ) {
        service.deleteAssignment(lessonId, assignmentId);
    }

    @GetMapping
    public List<AssignmentShortResponse> getAssignmentsByLessonId(
            @PathVariable Long lessonId
    ) {
        return service.getAssignmentsByLessonId(lessonId);
    }

    @GetMapping("/count")
    public Integer countAssignmentsByLessonId(
            @PathVariable Long lessonId
    ) {
        return service.countAssignmentsByLessonId(lessonId);
    }

}
