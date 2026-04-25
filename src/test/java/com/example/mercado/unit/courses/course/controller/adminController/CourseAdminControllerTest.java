package com.example.mercado.unit.courses.course.controller.adminController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.controller.CourseAdminController;
import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.service.interfaces.CourseAdminService;
import com.example.mercado.testUtils.courses.course.CourseTestData;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseAdminController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CourseAdminController Test")
public class CourseAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseAdminService service;


    @Test
    @DisplayName("Endpoint createCourse should create Course and return response")
    void createCourse_shouldCreateCourse_andReturnResponse() throws Exception {
        CreateCourseRequest request = CourseTestFactory.defaultCreateCourseRequest();

        CourseDetailsResponse response = CourseTestData.courseDetailsResponse(
                1L,
                "Java"
        );

        Mockito.when(service.createCourse(request))
                .thenReturn(response);

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    @DisplayName("Endpoint createCourse should return {BAD_REQUEST} when name is blank")
    void createCourse_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createCourseRequestWithBlankName();

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint createCourse should return {BAD_REQUEST} when type is null")
    void createCourse_shouldReturnBadRequest_whenTypeIsNull() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createCourseRequestWithNullType();

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint createCourse should return {BAD_REQUEST} when level is null")
    void createCourse_shouldReturnBadRequest_whenLevelIsNull() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createCourseRequestWithNullLevel();

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint createCourse should return {BAD_REQUEST} when price is null")
    void createCourse_shouldReturnBadRequest_whenPriceIsNull() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createCourseRequestWithNullPrice();

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint createCourse should return {BAD_REQUEST} when body is null")
    void createCourse_shouldReturnBadRequest_whenBodyIsNull() throws Exception {
        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint changeCourseStatus should correctly change course status")
    void changeCourseStatus_shouldCorrectlyChange_courseStatus() throws Exception {
        Long courseId = 1L;

        ChangeStatusRequest request = CourseTestFactory.customChangeStatusRequest(
                CourseStatus.PUBLISHED
        );

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(service).changeCourseStatus(courseId, request);
    }

    @Test
    @DisplayName("Endpoint changeCourseStatus should return {BAD_REQUEST} when status is null")
    void changeCourseStatus_shouldReturnBadRequest_whenStatusIsNull() throws Exception {
        Long courseId = 1L;

        String json = """
        {
          "status": null
        }
        """;

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never())
                .changeCourseStatus(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint changeCourseStatus should return {BAD_REQUEST} when body is null")
    void changeCourseStatus_shouldReturnBadRequest_whenBodyIsNull() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint changeCourseStatus should return {BAD_REQUEST} when courseId is invalid")
    void changeCourseStatus_shouldReturnBadRequest_whenCourseIdIsInvalid() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(put("/api/admin/courses/{courseId}/status", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).changeCourseStatus(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint updateCourse should update course")
    void updateCourse_shouldUpdateCourse() throws Exception {
        Long courseId = 1L;

        UpdateCourseRequest request = CourseTestFactory.customUpdateCourseRequestWithNull(
                "Java Core",
                null,
                null,
                null
        );

        CourseDetailsResponse response = CourseTestData.courseUpdatedDetailsResponseWithNull(
                1L,
                "Java Core",
                null,
                null,
                null
        );

        Mockito.when(service.updateCourse(courseId, request))
                .thenReturn(response);

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java Core"));

        Mockito.verify(service)
                .updateCourse(courseId, request);
    }

    @Test
    @DisplayName("Endpoint updateCourse should return {BAD_REQUEST} when name is empty")
    void updateCourse_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        Long courseId = 1L;

        UpdateCourseRequest request = CourseTestFactory.customUpdateCourseRequestWithNull(
                "",
                null,
                null,
                null
        );

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never())
                .updateCourse(courseId, request);
    }

    @Test
    @DisplayName("Endpoint updateCourse should update only price")
    void updateCourse_shouldUpdateOnlyPrice() throws Exception {
        Long courseId = 1L;

        String json = """
        {
          "price": 99
        }
        """;

        CourseDetailsResponse response = CourseTestData.courseUpdatedDetailsResponseWithNull(
                1L,
                null,
                null,
                null,
                BigDecimal.valueOf(99L)
        );

        Mockito.when(service.updateCourse(Mockito.eq(courseId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(99L));
    }

    @Test
    @DisplayName("Endpoint updateCourse should return {BAD_REQUEST} when body is empty")
    void updateCourse_shouldReturnBadRequest_whenBodyIsEmpty() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint updateCourse should return {BAD_REQUEST} when courseId is invalid")
    void updateCourse_shouldReturnBadRequest_whenCourseIdIsInvalid() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(patch("/api/admin/courses/{courseId}", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).updateCourse(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint changeCourseLevel should correctly change course level")
    void changeCourseLevel_shouldCorrectlyChange_courseLevel() throws Exception {
        Long courseId = 1L;

        ChangeLevelRequest request = CourseTestFactory.customChangeLevelRequest(
                CourseLevel.ADVANCED
        );

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(service).changeCourseLevel(courseId, request);
    }

    @Test
    @DisplayName("Endpoint changeCourseLevel should return {BAD_REQUEST} when level is null")
    void changeCourseLevel_shouldReturnBadRequest_whenLevelIsNull() throws Exception {
        Long courseId = 1L;

        String json = """
        {
          "level": null
        }
        """;

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never())
                .changeCourseLevel(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint changeCourseLevel should return {BAD_REQUEST} when body is null")
    void changeCourseLevel_shouldReturnBadRequest_whenBodyIsNull() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Endpoint changeCourseLevel should return {BAD_REQUEST} when courseId is invalid")
    void changeCourseLevel_shouldReturnBadRequest_whenCourseIdIsInvalid() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(put("/api/admin/courses/{courseId}/level", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).changeCourseLevel(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint deleteCourse should delete course")
    void deleteCourse_shouldDeleteCourse() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(delete("/api/admin/courses/{courseId}", courseId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteCourse(courseId);
    }

    @Test
    @DisplayName("Endpoint deleteCourse should return {BAD_REQUEST} when courseId is invalid")
    void deleteCourse_shouldReturnBadRequest_whenCourseIdIsInvalid() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(delete("/api/admin/courses/{courseId}", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).deleteCourse(Mockito.any());
    }

    @Test
    @DisplayName("Endpoint deleteCourse should return {COURSE_NOT_FOUND} when course doesnt exist")
    void deleteCourse_shouldReturnNotFound_whenCourseDoesNotExist() throws Exception {
        Long courseId = 1L;

        Mockito.doThrow(
                new AppException(
                        ErrorCode.COURSE_NOT_FOUND
                )).when(service).deleteCourse(courseId);

        mockMvc.perform(delete("/api/admin/courses/{courseId}", courseId))
                .andExpect(status().isNotFound());
    }

}
