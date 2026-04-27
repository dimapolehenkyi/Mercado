package com.example.mercado.integration.courses.courseRequirement.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.publicService.CoursePublicRequirementService;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CoursePublicRequirementServiceImplTest {

    @Autowired
    private CourseRequirementRepository repository;

    @Autowired
    private CoursePublicRequirementService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @DisplayName("getCourseRequirement should return correct Requirement when exists")
    void getCourseRequirement_shouldReturnRequirement_whenPointExists() {
        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("test")
                        .build()
        );

        RequirementResponse response = service.getCourseRequirement(
                1L,
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertNotNull(response),
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("test", response.text()),
                () -> Assertions.assertEquals(1, response.position())
        );
    }

    @Test
    @DisplayName("getCourseRequirement should throw {NOT_FOUND} when requirement doesnt exists")
    void getCourseRequirement_shouldThrowNotFoundException_whenRequirementDoesNotExist() {
        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.getCourseRequirement(1L, 1L)
        );

        Assertions.assertEquals(ErrorCode.REQUIREMENT_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("getAllByCourseId should return correct ordered Requirements when course has")
    void getAllByCourseId_shouldReturnOrderedRequirements_whenCourseHasLearningPoints() {
        List<CourseRequirement> requirements = List.of(
                repository.save(
                        CourseRequirementTestFactory.createDefaultCourseRequirement()
                                .text("test 1")
                                .position(1)
                                .build()
                ),
                repository.save(
                        CourseRequirementTestFactory.createDefaultCourseRequirement()
                                .text("test 3")
                                .position(3)
                                .build()
                ),
                repository.save(
                        CourseRequirementTestFactory.createDefaultCourseRequirement()
                                .text("test 2")
                                .position(2)
                                .build()
                )
        );

        List<RequirementResponse> responses = service.getAllByCourseId(
                1L
        );

        Assertions.assertAll(
                () -> Assertions.assertNotNull(responses),
                () -> Assertions.assertEquals(3, responses.size()),

                () -> Assertions.assertEquals(1, responses.get(0).position()),
                () -> Assertions.assertEquals("test 1", responses.get(0).text()),

                () -> Assertions.assertEquals(2, responses.get(1).position()),
                () -> Assertions.assertEquals("test 2", responses.get(1).text()),

                () -> Assertions.assertEquals(3, responses.get(2).position()),
                () -> Assertions.assertEquals("test 3", responses.get(2).text())
        );
    }

    @Test
    @DisplayName("getAllByCourseId should return empty list when course hasn't no one")
    void getAllByCourseId_shouldReturnEmptyList_whenCourseHasNoRequirements() {
        List<RequirementResponse> responses = service.getAllByCourseId(
                1L
        );

        Assertions.assertTrue(responses.isEmpty());
    }

}
