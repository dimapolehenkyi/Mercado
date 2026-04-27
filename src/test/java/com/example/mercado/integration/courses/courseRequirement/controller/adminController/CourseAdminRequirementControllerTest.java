package com.example.mercado.integration.courses.courseRequirement.controller.adminController;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.configs.TestSecurityConfig;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.adminService.CourseAdminRequirementService;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestFactory;
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
@DisplayName("CourseAdminRequirement Controller Test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class,
                TestSecurityConfig.class
        }
)
@ActiveProfiles("test")
public class CourseAdminRequirementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRequirementRepository repository;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @DisplayName("createCourseRequirement should return {201} and Requirement when valid request")
    void createCourseRequirement_shouldReturn201AndRequirement_whenValidRequest() throws  Exception {
        Long courseId = 1L;

        AddRequirementRequest request = new AddRequirementRequest(
                "Test"
        );

        mockMvc.perform(post("/admin/courses/{courseId}/requirements", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.text").value("Test"));

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Test", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("createCourseRequirement should return {400} when request invalid")
    void createCourseRequirement_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;

        AddRequirementRequest request = new AddRequirementRequest(
                "  "
        );

        mockMvc.perform(post("/admin/courses/{courseId}/requirements", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("createCourseRequirement should return {400} when courseId invalid")
    void createCourseRequirement_shouldReturn400_whenCourseIdInvalid() throws  Exception {
        Long courseId = -1L;

        AddRequirementRequest request = new AddRequirementRequest(
                "Test"
        );

        mockMvc.perform(post("/admin/courses/{courseId}/requirements", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("updateCourseRequirement should return {200} and update Requirement when valid request")
    void updateCourseRequirement_shouldReturn200AndUpdatedRequirement_whenValidRequest() throws  Exception {
        Long courseId = 1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirement.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.text").value("Updated"));

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Updated", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("updateCourseRequirement should return {400} when request invalid")
    void updateCourseRequirement_shouldReturn400_whenRequestInvalid() throws  Exception {
        Long courseId = 1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "   "
        );

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirement.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Test", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("updateCourseRequirement should return {404} when Requirement not found")
    void updateCourseRequirement_shouldReturn404_whenRequirementNotFound() throws  Exception {
        Long courseId = 1L;

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(put("/admin/courses/{courseId}/requirements/{requirementId}", courseId, 2L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, list.size()),
                () -> Assertions.assertEquals("Test", list.get(0).getText())
        );
    }

    @Test
    @DisplayName("deleteCourseRequirement should return {204} when Requirement exists")
    void deleteCourseRequirement_shouldReturn204_whenRequirementExists() throws  Exception {
        Long courseId = 1L;

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(delete("/admin/courses/{courseId}/requirements/{requirementId}", courseId, requirement.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("deleteCourseRequirement should return {404} when Requirement not found")
    void deleteCourseRequirement_shouldReturn404_whenRequirementNotFound() throws  Exception {
        Long courseId = 1L;

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );

        mockMvc.perform(delete("/admin/courses/{courseId}/requirements/{requirementId}", courseId, 2L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("updatePosition should return {200} and reorder Requirements when valid request")
    void updatePosition_shouldReturn200AndReorderedRequirements_whenValidRequest() throws  Exception {
        Long courseId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                3
        );

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirement1.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

    @Test
    @DisplayName("updatePosition should return {400} when position invalid")
    void updatePosition_shouldReturn400_whenPositionInvalid() throws  Exception {
        Long courseId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                -3
        );

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirement1.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

    @Test
    @DisplayName("updatePosition should return {404} when Requirement not found")
    void updatePosition_shouldReturn404_whenRequirementNotFound() throws  Exception {
        Long courseId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                3
        );

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, 5L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

    @Test
    @DisplayName("updatePosition should shift positions in database when moveDown")
    void updatePosition_shouldShiftPositionsInDatabase_whenMoveDown() throws  Exception {
        Long courseId = 1L;

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                1
        );

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 1")
                        .courseId(courseId)
                        .position(1)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 2")
                        .courseId(courseId)
                        .position(2)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test 3")
                        .courseId(courseId)
                        .position(3)
                        .build()
        );

        mockMvc.perform(patch("/admin/courses/{courseId}/requirements/{requirementId}/position", courseId, requirement3.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<CourseRequirement> list = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, list.size())
        );
    }

}
