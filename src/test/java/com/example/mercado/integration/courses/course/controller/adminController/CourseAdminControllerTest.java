package com.example.mercado.integration.courses.course.controller.adminController;

import com.example.mercado.courses.course.dto.ChangeLevelRequest;
import com.example.mercado.courses.course.dto.ChangeStatusRequest;
import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import com.example.mercado.testUtils.courses.course.CourseValidationTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CourseAdminController Integration Test")
public class CourseAdminControllerTest extends AbstractRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository repository;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createCourse_shouldCreateNewCourse_andReturnStatusCreated() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = "Java";
                    a.level = CourseLevel.BEGINNER;
                    a.type = CourseAccessType.PAID;
                    a.price = BigDecimal.valueOf(10);
                }
        );

        mockMvc.perform(post("/api/admin/courses")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.level").value(CourseLevel.BEGINNER.name()))
                .andExpect(jsonPath("$.type").value(CourseAccessType.PAID.name()))
                .andExpect(jsonPath("$.price").value(10.00));

        List<Course> courses = repository.findAll();

        Assertions.assertEquals(1, courses.size());

        Course saved = courses.getFirst();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Java", saved.getName()),
                () -> Assertions.assertEquals(CourseLevel.BEGINNER, saved.getLevel()),
                () -> Assertions.assertEquals(CourseAccessType.PAID, saved.getType()),
                () -> Assertions.assertEquals(0, saved.getPrice().compareTo(BigDecimal.TEN))
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createCourse_shouldThrowConflict_whenNameAlreadyExists() throws Exception {
        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.level(CourseLevel.BEGINNER);
                            a.type(CourseAccessType.PAID);
                            a.price(BigDecimal.valueOf(10));
                        }
                )
        );

        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = "Java";
                    a.level = CourseLevel.BEGINNER;
                    a.type = CourseAccessType.PAID;
                    a.price = BigDecimal.valueOf(10);
                }
        );

        mockMvc.perform(post("/api/admin/courses")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createCourse_shouldSetPriceZeroByDefault_whenTypeIsFree() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createCourseRequest(
                a -> {
                    a.name = "Java";
                    a.level = CourseLevel.BEGINNER;
                    a.type = CourseAccessType.FREE;
                    a.price = BigDecimal.valueOf(10);
                }
        );

        mockMvc.perform(post("/api/admin/courses")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(0));
    }

    @Test
    void createCourse_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {

        CreateCourseRequest request = CourseTestFactory.createCourseRequest(a -> {
            a.name = "Java";
            a.level = CourseLevel.BEGINNER;
            a.type = CourseAccessType.PAID;
            a.price = BigDecimal.TEN;
        });

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("resultOfValidationFailsInRequest")
    @WithMockUser(authorities = "ADMIN")
    void createCourse_shouldThrowBadRequest_whenRequestValidationFails(
            String json
    ) throws Exception {
        mockMvc.perform(post("/api/admin/courses")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest());
    }



    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateCourse_shouldReturnUpdatedCourse_whenValidRequest() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.level(CourseLevel.BEGINNER);
                    a.type(CourseAccessType.PAID);
                    a.price(BigDecimal.valueOf(10));
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(a -> {
            a.name = "Python";
            a.level = CourseLevel.ADVANCED;
            a.type = CourseAccessType.FREE;
            a.price = BigDecimal.ZERO;
        });

        mockMvc.perform(patch("/api/admin/courses/{courseId}", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Python"))
                .andExpect(jsonPath("$.level").value(CourseLevel.ADVANCED.name()))
                .andExpect(jsonPath("$.type").value(CourseAccessType.FREE.name()))
                .andExpect(jsonPath("$.price").value(0));

        List<Course> courses = repository.findAll();

        Assertions.assertEquals(1, courses.size());

        Course updated = courses.getFirst();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Python", updated.getName()),
                () -> Assertions.assertEquals(CourseLevel.ADVANCED, updated.getLevel()),
                () -> Assertions.assertEquals(CourseAccessType.FREE, updated.getType()),
                () -> Assertions.assertEquals(0, updated.getPrice().compareTo(BigDecimal.ZERO))
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateCourse_shouldThrowBadRequestAndNotUpdate_whenNameIsBlank() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.level(CourseLevel.BEGINNER);
                    a.type(CourseAccessType.PAID);
                    a.price(BigDecimal.valueOf(10));
                }
        );

        repository.save(course);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(a -> {
            a.name = "";
            a.level = CourseLevel.ADVANCED;
            a.type = CourseAccessType.FREE;
            a.price = BigDecimal.ZERO;
        });

        mockMvc.perform(patch("/api/admin/courses/{courseId}", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<Course> courses = repository.findAll();

        Assertions.assertEquals(1, courses.size());

        Course updated = courses.getFirst();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Java", updated.getName()),
                () -> Assertions.assertEquals(CourseLevel.BEGINNER, updated.getLevel()),
                () -> Assertions.assertEquals(CourseAccessType.PAID, updated.getType()),
                () -> Assertions.assertEquals(0, updated.getPrice().compareTo(BigDecimal.valueOf(10)))
        );
    }

    @Test
    void updateCourse_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        Long courseId = 1L;

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest(a -> {
            a.name = "Java";
            a.level = CourseLevel.BEGINNER;
            a.type = CourseAccessType.PAID;
            a.price = BigDecimal.TEN;
        });

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateCourse_shouldThrowBadRequest_whenIdIsNegative() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseStatus_shouldReturnCourseWithChangedStatusAndNoContent_whenValidRequest() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.level(CourseLevel.BEGINNER);
                    a.type(CourseAccessType.PAID);
                    a.price(BigDecimal.valueOf(10));
                    a.status(CourseStatus.PUBLISHED);
                }
        );

        repository.save(course);

        ChangeStatusRequest request = new ChangeStatusRequest(CourseStatus.CLOSED);

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        List<Course> courses = repository.findAll();

        Assertions.assertEquals(1, courses.size());

        Course changed = courses.getFirst();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Java", changed.getName()),
                () -> Assertions.assertEquals(CourseLevel.BEGINNER, changed.getLevel()),
                () -> Assertions.assertEquals(CourseAccessType.PAID, changed.getType()),
                () -> Assertions.assertEquals(0, changed.getPrice().compareTo(BigDecimal.valueOf(10))),
                () -> Assertions.assertEquals(CourseStatus.CLOSED, changed.getStatus())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseStatus_shouldThrowException_whenCourseNotFound() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest(null);

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", 1L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseStatus_shouldThrowBadRequest_whenRequestValidationFails() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );

        repository.save(course);

        ChangeStatusRequest request = new ChangeStatusRequest(null);

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseStatus_shouldThrowBadRequest_whenCourseWasDraftAndRequestIsClosed() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.status(CourseStatus.DRAFT);
                }
        );

        repository.save(course);

        ChangeStatusRequest request = new ChangeStatusRequest(CourseStatus.CLOSED);

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseStatus_shouldThrowBadRequest_whenIdIsNegative() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeCourseStatus_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        Long courseId = 1L;

        ChangeStatusRequest request = new ChangeStatusRequest(CourseStatus.PUBLISHED);

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseLevel_shouldReturnCourseWithChangedLevelAndNoContent_whenValidRequest() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                    a.level(CourseLevel.BEGINNER);
                    a.type(CourseAccessType.PAID);
                    a.price(BigDecimal.valueOf(10));
                }
        );

        repository.save(course);

        ChangeLevelRequest request = new ChangeLevelRequest(CourseLevel.INTERMEDIATE);

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        List<Course> courses = repository.findAll();

        Assertions.assertEquals(1, courses.size());

        Course changed = courses.getFirst();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Java", changed.getName()),
                () -> Assertions.assertEquals(CourseLevel.INTERMEDIATE, changed.getLevel()),
                () -> Assertions.assertEquals(CourseAccessType.PAID, changed.getType()),
                () -> Assertions.assertEquals(0, changed.getPrice().compareTo(BigDecimal.valueOf(10)))
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseLevel_shouldThrowException_whenCourseNotFound() throws Exception {
        ChangeLevelRequest request = new ChangeLevelRequest(CourseLevel.INTERMEDIATE);

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", 1L)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseLevel_shouldThrowBadRequest_whenRequestValidationFails() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );

        repository.save(course);

        ChangeLevelRequest request = new ChangeLevelRequest(null);

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void changeCourseLevel_shouldThrowBadRequest_whenIdIsNegative() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeCourseLevel_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        Long courseId = 1L;

        ChangeLevelRequest request = new ChangeLevelRequest(CourseLevel.BEGINNER);

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteCourse_shouldReturnCourseWithStatusClosed_whenValidRequest() throws Exception {
        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java");
                }
        );

        repository.save(course);

        mockMvc.perform(delete("/api/admin/courses/{courseId}", course.getId())
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Course deleted = repository.findById(course.getId()).get();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(deleted),
                () -> Assertions.assertEquals(CourseStatus.CLOSED, deleted.getStatus())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteCourse_shouldThrowException_whenCourseNotFound() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(delete("/api/admin/courses/{courseId}", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteCourse_shouldThrowException_whenIdIsNegative() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(delete("/api/admin/courses/{courseId}", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCourse_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(delete("/api/admin/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    static Stream<String> resultOfValidationFailsInRequest() {
        return CourseValidationTestData.resultOfValidationFailsInRequest();
    }
}
