package com.example.mercado.courses.assignment.mapper;

import com.example.mercado.courses.assignment.dto.AssignmentResponse;
import com.example.mercado.courses.assignment.dto.AssignmentShortResponse;
import com.example.mercado.courses.assignment.dto.CreateAssignmentRequest;
import com.example.mercado.courses.assignment.dto.UpdateAssignmentRequest;
import com.example.mercado.courses.assignment.entity.Assignment;
import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AssignmentMapper Test")
public class AssignmentMapperTest {

    private AssignmentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AssignmentMapper();
    }


    @Test
    @DisplayName("Func toEntity should map all fields")
    void toEntity_shouldMapAllFields() {
        Long lessonId = 1L;
        CreateAssignmentRequest request = new CreateAssignmentRequest(
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100
        );

        Assignment assignment = mapper.toEntity(lessonId, request);

        Assertions.assertNotNull(assignment);
        Assertions.assertEquals("Test", assignment.getName());
        Assertions.assertEquals("Test description", assignment.getDescription());
        Assertions.assertEquals(AssignmentType.TEST, assignment.getType());
        Assertions.assertEquals(AssignmentStatus.ASSIGNED, assignment.getStatus());
        Assertions.assertEquals(100, assignment.getMaxScore());
    }

    @Test
    @DisplayName("Func updateEntity should map all fields")
    void updateEntity_shouldMapAllFields() {
        Long lessonId = 1L;

        Assignment assignment = Assignment.builder()
                .lessonId(lessonId)
                .name("Test")
                .description("Test description")
                .type(AssignmentType.TEST)
                .status(AssignmentStatus.ASSIGNED)
                .maxScore(100)
                .position(1)
                .build();

        UpdateAssignmentRequest request = new UpdateAssignmentRequest(
                "Updated test",
                "Updated description",
                AssignmentType.PRACTICE,
                AssignmentStatus.GRADED,
                80
        );

        mapper.updateEntity(assignment, request);

        Assertions.assertNotNull(assignment);
        Assertions.assertEquals("Updated test", assignment.getName());
        Assertions.assertEquals("Updated description", assignment.getDescription());
        Assertions.assertEquals(AssignmentType.PRACTICE, assignment.getType());
        Assertions.assertEquals(AssignmentStatus.GRADED, assignment.getStatus());
        Assertions.assertEquals(80, assignment.getMaxScore());

    }

    @Test
    @DisplayName("Func toResponse should map all fields")
    void toResponse_shouldMapAllFields() {
        Long lessonId = 1L;

        Assignment assignment = Assignment.builder()
                .lessonId(lessonId)
                .name("Test")
                .description("Test description")
                .type(AssignmentType.TEST)
                .status(AssignmentStatus.ASSIGNED)
                .maxScore(100)
                .position(1)
                .build();

        AssignmentResponse testResponse = new AssignmentResponse(
                null,
                1L,
                "Test",
                "Test description",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        AssignmentResponse response = mapper.toResponse(assignment);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(testResponse, response);
    }

    @Test
    @DisplayName("Func toShortResponse should map all fields")
    void toShortResponse_shouldMapAllFields() {
        Long lessonId = 1L;

        Assignment assignment = Assignment.builder()
                .lessonId(lessonId)
                .name("Test")
                .description("Test description")
                .type(AssignmentType.TEST)
                .status(AssignmentStatus.ASSIGNED)
                .maxScore(100)
                .position(1)
                .build();

        AssignmentShortResponse testResponse = new AssignmentShortResponse(
                null,
                "Test",
                AssignmentType.TEST,
                AssignmentStatus.ASSIGNED,
                100,
                1
        );

        AssignmentShortResponse response = mapper.toShortResponse(assignment);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(testResponse, response);
    }
}
