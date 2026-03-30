package com.example.mercado.courses.assignment.service.interfaces;

import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;

import java.util.List;

public interface AssignmentService {

    AssignmentResponse createAssignment(Long lessonId, CreateAssignmentRequest request);
    AssignmentResponse updateAssignment(Long lessonId, Long assignmentId, UpdateAssignmentRequest request);
    AssignmentResponse getAssignment(Long lessonId, Long assignmentId);

    void deleteAssignment(Long lessonId, Long assignmentId);

    List<AssignmentShortResponse> getAssignmentsByLessonId(Long lessonId);

    Integer countAssignmentsByLessonId(Long lessonId);

}
