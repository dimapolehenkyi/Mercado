package com.example.mercado.courses.assignment.service;


import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;
import com.example.mercado.courses.assignment.entity.Assignment;
import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;
import com.example.mercado.courses.assignment.mapper.AssignmentMapper;
import com.example.mercado.courses.assignment.repository.AssignmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssignmentService Test")
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository repository;

    @Mock
    private AssignmentMapper mapper;

    @InjectMocks
    private AssignmentServiceImpl service;

    @Test
    @DisplayName("Func createAssignment should return response and create assignment")
    void createAssignment_shouldReturnResponse_andCreateAssignment() {
        Long lessonId = 1L;

        Assignment assignment = new Assignment();

        CreateAssignmentRequest request = new CreateAssignmentRequest(
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100
        );

        AssignmentResponse testResponse = new AssignmentResponse(
                1L,
                1L,
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        Mockito.when(repository.existsByNameAndLessonId(request.name(), lessonId)).thenReturn(false);
        Mockito.when(repository.save(assignment)).thenReturn(assignment);
        Mockito.when(repository.countByLessonId(lessonId)).thenReturn(0);
        Mockito.when(mapper.toEntity(lessonId, request)).thenReturn(assignment);
        Mockito.when(mapper.toResponse(assignment)).thenReturn(testResponse);

        AssignmentResponse result = service.createAssignment(lessonId, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(testResponse, result);

        Mockito.verify(repository, Mockito.times(1)).save(assignment);
        Mockito.verify(repository, Mockito.times(1)).countByLessonId(lessonId);
        Mockito.verify(mapper, Mockito.times(1)).toEntity(lessonId, request);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(assignment);
    }

    @Test
    @DisplayName("Func updateAssignment should return response and update assignment")
    void updateAssignment_shouldReturnResponse_andUpdateAssignment() {
        Long lessonId = 1L;
        Long assignmentId = 1L;

        Assignment assignment = new Assignment();

        UpdateAssignmentRequest request = new UpdateAssignmentRequest(
                "Updated test",
                "Updated test description",
                AssignmentType.PRACTICE,
                AssignmentStatus.OVERDUE,
                80
        );

        AssignmentResponse testResponse = new AssignmentResponse(
                1L,
                1L,
                "Updated test",
                "Updated test description",
                AssignmentType.PRACTICE,
                AssignmentStatus.OVERDUE,
                80,
                1
        );

        Mockito.when(repository.findByIdAndLessonId(assignmentId, lessonId)).thenReturn(Optional.of(assignment));
        Mockito.when(repository.save(assignment)).thenReturn(assignment);
        Mockito.when(mapper.toResponse(assignment)).thenReturn(testResponse);

        AssignmentResponse response = service.updateAssignment(assignmentId, lessonId, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(testResponse, response);

        Mockito.verify(repository, Mockito.times(1)).findByIdAndLessonId(assignmentId, lessonId);
        Mockito.verify(repository, Mockito.times(1)).save(assignment);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(assignment);
    }

    @Test
    @DisplayName("Func getAssignment should return response")
    void getAssignment_shouldReturnResponse() {
        Long lessonId = 1L;
        Long assignmentId = 1L;

        Assignment assignment = new Assignment();

        AssignmentResponse testResponse = new AssignmentResponse(
                1L,
                1L,
                "Test",
                "Test description",
                AssignmentType.PROJECT,
                AssignmentStatus.NOT_ASSIGNED,
                100,
                1
        );

        Mockito.when(repository.findByIdAndLessonId(assignmentId, lessonId)).thenReturn(Optional.of(assignment));
        Mockito.when(mapper.toResponse(assignment)).thenReturn(testResponse);

        AssignmentResponse response = service.getAssignment(assignmentId, lessonId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(testResponse, response);
    }

    @Test
    @DisplayName("Func deleteAssignment should delete assignment")
    void deleteAssignment_shouldDeleteAssignment() {
        Long lessonId = 1L;
        Long assignmentId = 1L;

        Assignment assignment = new Assignment();
        Mockito.when(repository.findByIdAndLessonId(assignmentId, lessonId)).thenReturn(Optional.of(assignment));

        service.deleteAssignment(assignmentId, lessonId);

        Mockito.verify(repository, Mockito.times(1)).findByIdAndLessonId(assignmentId, lessonId);
        Mockito.verify(repository, Mockito.times(1)).delete(assignment);
    }

    @Test
    @DisplayName("Func getAssignmentsByLessonId should return list of AssignmentShortResponse")
    void getAssignmentsByLessonId_shouldReturnList_ofAssignmentsShortResponse() {
        Long lessonId = 1L;

        List<Assignment> assignments = List.of(
                Assignment.builder()
                        .lessonId(lessonId)
                        .name("Test 1")
                        .description("Test description 1")
                        .type(AssignmentType.TEST)
                        .status(AssignmentStatus.ASSIGNED)
                        .maxScore(100)
                        .position(1)
                        .build(),
                Assignment.builder()
                        .lessonId(lessonId)
                        .name("Test 2")
                        .description("Test description 2")
                        .type(AssignmentType.THEORY)
                        .status(AssignmentStatus.OVERDUE)
                        .maxScore(80)
                        .position(2)
                        .build()
        );

        AssignmentShortResponse testResponse1 = new AssignmentShortResponse(
                1L,
                "Test 1",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        AssignmentShortResponse testResponse2 = new AssignmentShortResponse(
                1L,
                "Test 2",
                AssignmentType.THEORY,
                AssignmentStatus.OVERDUE,
                80,
                2
        );

        Mockito.when(repository.findAllByLessonIdOrderByPositionAsc(lessonId)).thenReturn(assignments);
        Mockito.when(mapper.toShortResponse(assignments.get(0))).thenReturn(testResponse1);
        Mockito.when(mapper.toShortResponse(assignments.get(1))).thenReturn(testResponse2);

        List<AssignmentShortResponse> response = service.getAssignmentsByLessonId(lessonId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(testResponse1, response.get(0));
        Assertions.assertEquals(testResponse2, response.get(1));

        Mockito.verify(repository, Mockito.times(1)).findAllByLessonIdOrderByPositionAsc(lessonId);
        Mockito.verify(mapper, Mockito.times(1)).toShortResponse(assignments.get(0));
        Mockito.verify(mapper, Mockito.times(1)).toShortResponse(assignments.get(1));
    }

    @Test
    @DisplayName("Func countAssignmentsByLessonId should return count of assignments in lesson")
    void countAssignmentsByLessonId_shouldReturnCount() {
        Long lessonId = 1L;

        Mockito.when(repository.countByLessonId(lessonId)).thenReturn(3);

        Integer count = service.countAssignmentsByLessonId(lessonId);

        Assertions.assertNotNull(count);
        Assertions.assertEquals(3, count);
    }

}
