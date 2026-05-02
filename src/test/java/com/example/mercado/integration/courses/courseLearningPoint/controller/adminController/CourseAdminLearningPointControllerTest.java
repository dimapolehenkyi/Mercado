package com.example.mercado.integration.courses.courseLearningPoint.controller.adminController;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.configs.TestSecurityConfig;
import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.testUtils.courses.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CourseAdminLP Controller Test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class,
                TestSecurityConfig.class
        }
)
@ActiveProfiles("test")
public class CourseAdminLearningPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseLearningPointRepository repository;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @DisplayName("createCourseLearningPoint should return {201} and LearningPoint when valid request")
    void createCourseLearningPoint_shouldReturn201AndLearningPoint_whenValidRequest() throws  Exception {
        Long courseId = 1L;

        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test"
        );

        mockMvc.perform(post("/admin/courses/{courseId}/learning-points", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.text").value("Test"));

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Test", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("createCourseLearningPoint should return {400} when request invalid")
    void createCourseLearningPoint_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;

        AddLearningPointRequest request = new AddLearningPointRequest(
                "  "
        );

        mockMvc.perform(post("/admin/courses/{courseId}/learning-points", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("createCourseLearningPoint should return {400} when courseId invalid")
    void createCourseLearningPoint_shouldReturn400_whenCourseIdInvalid() throws  Exception {
        Long courseId = -1L;

        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test"
        );

        mockMvc.perform(post("/admin/courses/{courseId}/learning-points", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {200} and update LearningPoint when valid request")
    void updateCourseRequirement_shouldReturn200AndUpdatedLearningPoint_whenValidRequest() throws  Exception {
        Long courseId = 1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(put("/admin/courses/{courseId}/learning-points/{pointId}", courseId, point.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.text").value("Updated"));

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Updated", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {400} when request invalid")
    void updateCourseLearningPoint_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "   "
        );

        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(put("/admin/courses/{courseId}/learning-points/{pointId}", courseId, point.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Test", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("updateCourseLearningPoint should return {404} when LearningPoint not found")
    void updateCourseLearningPoint_shouldReturn404_whenLearningPointNotFound() throws  Exception {
        Long courseId = 1L;

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(put("/admin/courses/{courseId}/learning-points/{pointId}", courseId, 2L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Test", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should return {204} when LearningPoint exists")
    void deleteCourseLearningPoint_shouldReturn204_whenLearningPointExists() throws  Exception {
        Long courseId = 1L;

        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(delete("/admin/courses/{courseId}/learning-points/{pointId}", courseId, point.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("deleteCourseLearningPoint should return {404} when LearningPoint not found")
    void deleteCourseLearningPoint_shouldReturn404_whenLearningPointNotFound() throws  Exception {
        Long courseId = 1L;

        CourseLearningPoint point = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(delete("/admin/courses/{courseId}/learning-points/{pointId}", courseId, 2L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("updatePosition should return {200} and reorder LearningPoints when valid request")
    void updatePosition_shouldReturn200AndReorderedLearningPoints_whenValidRequest() throws  Exception {
        Long courseId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                3
        );

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/learning-points/{pointId}/position", courseId, point1.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

    @Test
    @DisplayName("updatePosition should return {400} when position invalid")
    void updatePosition_shouldReturn400_whenPositionInvalid() throws  Exception {
        Long courseId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                -3
        );

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/learning-points/{pointId}/position", courseId, point1.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

    @Test
    @DisplayName("updatePosition should return {404} when LearningPoint not found")
    void updatePosition_shouldReturn404_whenLearningPointNotFound() throws  Exception {
        Long courseId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                3
        );

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/learning-points/{pointId}/position", courseId, 5L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

    @Test
    @DisplayName("updatePosition should shift positions in database when moveDown")
    void updatePosition_shouldShiftPositionsInDatabase_whenMoveDown() throws  Exception {
        Long courseId = 1L;

        ReorderLearningPointRequest request = new ReorderLearningPointRequest(
                1
        );

        CourseLearningPoint point1 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseLearningPoint point2 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseLearningPoint point3 = repository.save(
                CourseLPTestFactory.createDefaultCourseLP()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/learning-points/{pointId}/position", courseId, point3.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<CourseLearningPoint> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

}
