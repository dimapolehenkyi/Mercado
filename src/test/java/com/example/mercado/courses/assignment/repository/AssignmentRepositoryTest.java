package com.example.mercado.courses.assignment.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.assignment.entity.Assignment;
import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("AssignmentRepository Test")
public class AssignmentRepositoryTest {


    @Autowired
    private AssignmentRepository repository;



    @Test
    @DisplayName("Func existsByNameAndLessonId should return true if exist")
    void existsByNameAndLessonId_shouldReturnTrue() {
        Long lessonId = 1L;
        String name = "Test";

        Assignment assignment = Assignment.builder()
                .lessonId(lessonId)
                .name(name)
                .description("Test description")
                .type(AssignmentType.TEST)
                .status(AssignmentStatus.ASSIGNED)
                .maxScore(100)
                .position(1)
                .build();

        repository.save(assignment);

        boolean exist = repository.existsByNameAndLessonId(name, lessonId);

        Assertions.assertTrue(exist);
    }

    @Test
    @DisplayName("Func countByLessonId should return count of assignments")
    void countByLessonId_shouldReturnCount() {
        Long lessonId = 1L;

        Assignment assignment1 = Assignment.builder()
                .lessonId(lessonId)
                .name("Test 1")
                .description("Test description 1")
                .type(AssignmentType.TEST)
                .status(AssignmentStatus.ASSIGNED)
                .maxScore(100)
                .position(1)
                .build();

        Assignment assignment2 = Assignment.builder()
                .lessonId(lessonId)
                .name("Test 2")
                .description("Test description 2")
                .type(AssignmentType.PRACTICE)
                .status(AssignmentStatus.GRADED)
                .maxScore(80)
                .position(2)
                .build();

        repository.saveAll(List.of(assignment1, assignment2));

        Integer count = repository.countByLessonId(lessonId);

        Assertions.assertNotNull(count);
        Assertions.assertEquals(2, count);
    }

    @Test
    @DisplayName("Func findByIdAndLessonId should return optional of assignments")
    void findByIdAndLessonId_shouldReturnOptional_ofAssignment() {
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

        repository.save(assignment);

        Optional<Assignment> founded = repository.findByIdAndLessonId(assignment.getId(), lessonId);

        Assertions.assertTrue(founded.isPresent());
    }

    @Test
    @DisplayName("Func findAllByLessonIdOrderByPositionAsc should return list of assignments")
    void findAllByLessonIdOrderByPositionAsc_shouldReturnList_ofAssignments() {
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

        repository.saveAll(assignments);

        List<Assignment> results = repository.findAllByLessonIdOrderByPositionAsc(lessonId);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(1, results.get(0).getPosition());
        Assertions.assertEquals(2, results.get(1).getPosition());
        Assertions.assertTrue(results.stream().allMatch(a -> a.getLessonId().equals(lessonId)));
    }

}
