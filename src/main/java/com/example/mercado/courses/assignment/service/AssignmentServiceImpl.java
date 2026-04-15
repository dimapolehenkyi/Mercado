package com.example.mercado.courses.assignment.service;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;
import com.example.mercado.courses.assignment.entity.Assignment;
import com.example.mercado.courses.assignment.mapper.AssignmentMapper;
import com.example.mercado.courses.assignment.repository.AssignmentRepository;
import com.example.mercado.courses.assignment.service.interfaces.AssignmentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository repository;

    private final AssignmentMapper mapper;

    @Override
    public AssignmentResponse createAssignment(Long lessonId, CreateAssignmentRequest request) {
        if (repository.existsByNameAndLessonId(request.name(), lessonId)) {
            throw new AppException(
                    ErrorCode.ASSIGNMENT_ALREADY_EXISTS,
                    request.name()
            );
        }

        Assignment assignment = mapper.toEntity(lessonId, request);
        assignment.setPosition(repository.countByLessonId(lessonId) + 1);
        Assignment savedAssignment = repository.save(assignment);

        return mapper.toResponse(savedAssignment);
    }

    @Override
    public AssignmentResponse updateAssignment(Long lessonId, Long assignmentId, UpdateAssignmentRequest request) {
        Assignment assignment = getAssignmentOrThrow(lessonId, assignmentId);

        updateIfChanged(request.name(), assignment.getName(), assignment::setName);
        updateIfChanged(request.description(), assignment.getDescription(), assignment::setDescription);
        updateIfChanged(request.type(), assignment.getType(), assignment::setType);
        updateIfChanged(request.status(), assignment.getStatus(), assignment::setStatus);
        updateIfChanged(request.maxScore(), assignment.getMaxScore(), assignment::setMaxScore);

        Assignment savedAssignment = repository.save(assignment);

        return mapper.toResponse(savedAssignment);
    }

    @Override
    public AssignmentResponse getAssignment(Long lessonId, Long assignmentId) {
        Assignment assignment = getAssignmentOrThrow(lessonId, assignmentId);

        return mapper.toResponse(assignment);
    }

    @Override
    public void deleteAssignment(Long lessonId, Long assignmentId) {
        Assignment assignment = getAssignmentOrThrow(lessonId, assignmentId);
        repository.delete(assignment);
    }

    @Override
    public List<AssignmentShortResponse> getAssignmentsByLessonId(Long lessonId) {
        List<Assignment> assignments = repository.findAllByLessonIdOrderByPositionAsc(lessonId);

        return assignments.stream()
                .map(mapper::toShortResponse)
                .toList();
    }

    @Override
    public Integer countAssignmentsByLessonId(Long lessonId) {
        return repository.countByLessonId(lessonId);
    }




    private <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, currentValue)) {
            setter.accept(newValue);
        }
    }

    private @NonNull Assignment getAssignmentOrThrow(@NonNull Long lessonId, @NonNull Long id) {
        return repository.findByIdAndLessonId(id, lessonId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.ASSIGNMENT_NOT_FOUND,
                        id
                ));
    }

}
