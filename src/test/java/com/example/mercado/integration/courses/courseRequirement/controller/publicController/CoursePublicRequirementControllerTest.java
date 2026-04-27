package com.example.mercado.integration.courses.courseRequirement.controller.publicController;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.publicService.CoursePublicRequirementService;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CoursePublicRequirement Controller Test")
@ActiveProfiles("test")
public class CoursePublicRequirementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRequirementRepository repository;

    @Autowired
    private CoursePublicRequirementService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @DisplayName("getCourseRequirement should return 200 and Requirement when exists")
    void getCourseRequirement_shouldReturn200AndRequirement_whenRequirementExists() throws  Exception {
        Long courseId = 1L;

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .courseId(1L)
                        .text("test text")
                        .position(1)
                        .build()
        );

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}",
                        courseId, requirement.getId()
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requirement.getId()))
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.position").value(1));
    }

    @Test
    @DisplayName("getCourseRequirement should return 404 when Requirement doesn't exists")
    void getCourseRequirement_shouldReturn404_whenRequirementDoesNotExist() throws  Exception {
        Long courseId = 1L;
        Long requirementId = 1L;

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}",
                        courseId, requirementId
                ))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getCourseRequirement should return 400 when courseId negative")
    void getCourseRequirement_shouldReturn400_whenCourseIdIsNotPositive() throws  Exception {
        Long courseId = -1L;
        Long requirementId = 1L;

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}",
                        courseId, requirementId
                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getCourseRequirement should return 400 when requirementId negative")
    void getCourseRequirement_shouldReturn400_whenRequirementIdIsNotPositive() throws  Exception {
        Long courseId = 1L;
        Long requirementId = -1L;

        mockMvc.perform(get("/courses/{courseId}/requirements/{requirementId}",
                        courseId, requirementId
                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getAllByCourseId should return 200 and ordered list of Requirements when points exist")
    void getAllByCourseId_shouldReturn200AndOrderedRequirements_whenPointsExist() throws  Exception {
        Long courseId = 1L;

        List<CourseRequirement> responses = List.of(
                repository.save(
                        CourseRequirementTestFactory.createDefaultCourseRequirement()
                                .position(1)
                                .text("test text 1")
                                .courseId(courseId)
                                .build()
                ),
                repository.save(
                        CourseRequirementTestFactory.createDefaultCourseRequirement()
                                .position(3)
                                .text("test text 3")
                                .courseId(courseId)
                                .build()
                ),
                repository.save(
                        CourseRequirementTestFactory.createDefaultCourseRequirement()
                                .position(2)
                                .text("test text 2")
                                .courseId(courseId)
                                .build()
                )
        );

        mockMvc.perform(get("/courses/{courseId}/requirements", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].text").value("test text 1"))
                .andExpect(jsonPath("$[1].text").value("test text 2"))
                .andExpect(jsonPath("$[2].text").value("test text 3"));
    }

    @Test
    @DisplayName("getAllByCourseId should return 200 and empty list when no one Requirements exist")
    void getAllByCourseId_shouldReturn200AndEmptyList_whenNoRequirementsExist() throws  Exception {
        Long courseId = 1L;

        List<CourseRequirement> responses = List.of();

        mockMvc.perform(get("/courses/{courseId}/requirements", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("getAllByCourseId should return 400 when courseId negative")
    void getAllByCourseId_shouldReturn400_whenCourseIdIsNotPositive() throws  Exception {
        Long courseId = -1L;

        mockMvc.perform(get("/courses/{courseId}/requirements",
                        courseId
                ))
                .andExpect(status().isBadRequest());
    }

}
