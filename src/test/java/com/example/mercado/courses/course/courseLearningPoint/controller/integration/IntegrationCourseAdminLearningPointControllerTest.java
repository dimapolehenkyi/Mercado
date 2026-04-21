package com.example.mercado.courses.course.courseLearningPoint.controller.integration;

import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class IntegrationCourseAdminLearningPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseLearningPointRepository repository;

    @Autowired
    private CourseAdminLearningPointService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("addCourseLearningPoint should return {201} and LearningPoint when valid request")
    void addCourseLearningPoint_shouldReturn201AndLearningPoint_whenValidRequest() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("addCourseLearningPoint should return {400} when request invalid")
    void addCourseLearningPoint_shouldReturn400_whenRequestInvalid() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("addCourseLearningPoint should return {400} when courseId invalid")
    void addCourseLearningPoint_shouldReturn400_whenCourseIdInvalid() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("addCourseLearningPoint should persist LearningPoint when valid request")
    void addCourseLearningPoint_shouldPersistLearningPoint_whenValidRequest() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseLearningPoint should return {200} and update LearningPoint when valid request")
    void updateCourseRequirement_shouldReturn200AndUpdatedLearningPoint_whenValidRequest() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseLearningPoint should return {400} when request invalid")
    void updateCourseLearningPoint_shouldReturn400_whenRequestInvalid() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseLearningPoint should return {404} when LearningPoint not found")
    void updateCourseLearningPoint_shouldReturn404_whenLearningPointNotFound() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseLearningPoint should update LearningPoint in database when valid request")
    void updateCourseLearningPoint_shouldUpdateLearningPointInDatabase_whenValidRequest() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourseLearningPoint should return {204} when LearningPoint exists")
    void deleteCourseLearningPoint_shouldReturn204_whenLearningPointExists() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourseLearningPoint should return {404} when LearningPoint not found")
    void deleteCourseLearningPoint_shouldReturn404_whenLearningPointNotFound() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourseLearningPoint should delete LearningPoint from database when exists")
    void deleteCourseLearningPoint_shouldDeleteLearningPointFromDatabase_whenExists() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should return {200} and reorder LearningPoints when valid request")
    void updatePosition_shouldReturn200AndReorderedLearningPoints_whenValidRequest() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should return {400} when position invalid")
    void updatePosition_shouldReturn400_whenPositionInvalid() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should return {404} when LearningPoint not found")
    void updatePosition_shouldReturn404_whenLearningPointNotFound() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should shift positions in database when moveUp")
    void updatePosition_shouldShiftPositionsInDatabase_whenMoveUp() throws  Exception {}

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should shift positions in database when moveDown")
    void updatePosition_shouldShiftPositionsInDatabase_whenMoveDown() throws  Exception {}

}
