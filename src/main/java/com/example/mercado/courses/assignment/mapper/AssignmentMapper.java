package com.example.mercado.courses.assignment.mapper;

import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;
import com.example.mercado.courses.assignment.entity.Assignment;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {

    public Assignment toEntity(@NonNull Long lessonId, @NonNull CreateAssignmentRequest request) {
        return Assignment.builder()
                .lessonId(lessonId)
                .name(request.name())
                .description(request.description())
                .status(request.status())
                .type(request.type())
                .maxScore(request.maxScore())
                .build();
    }

    public void updateEntity(@NonNull Assignment assignment, @NonNull UpdateAssignmentRequest request) {
        assignment.setName(request.name());
        assignment.setDescription(request.description());
        assignment.setStatus(request.status());
        assignment.setType(request.type());
        assignment.setMaxScore(request.maxScore());
    }

    public AssignmentResponse toResponse(@NonNull Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getLessonId(),
                assignment.getName(),
                assignment.getDescription(),
                assignment.getType(),
                assignment.getStatus(),
                assignment.getMaxScore(),
                assignment.getPosition()
        );
    }

    public AssignmentShortResponse toShortResponse(@NonNull Assignment assignment) {
        return new AssignmentShortResponse(
                assignment.getId(),
                assignment.getName(),
                assignment.getType(),
                assignment.getStatus(),
                assignment.getMaxScore(),
                assignment.getPosition()
        );
    }

}
