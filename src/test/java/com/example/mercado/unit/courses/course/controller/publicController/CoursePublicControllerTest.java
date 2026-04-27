package com.example.mercado.unit.courses.course.controller.publicController;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.controller.CoursePublicController;
import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.service.interfaces.CoursePublicService;
import com.example.mercado.testUtils.courses.course.CourseTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoursePublicController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CoursePublicController Test")
public class CoursePublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoursePublicService service;


    @Test
    @DisplayName("Endpoint getCourse should return course")
    void getCourse_shouldReturnCourse() throws Exception {
        CourseDetailsResponse response = CourseTestData.courseDetailsResponse(1L, "Java");

        Mockito.when(service.getActiveCourseById(Mockito.anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    @DisplayName("Endpoint getCourse should return {ERROR_404} when course not found")
    void getCourse_shouldReturn404_whenNotFound() throws Exception {
        Long courseId = 1L;

        Mockito.when(service.getActiveCourseById(courseId))
                .thenThrow(
                        new AppException(
                                ErrorCode.COURSE_NOT_FOUND)
                );

        mockMvc.perform(get("/api/courses/{courseId}", courseId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Endpoint getCourse should return {BAD_REQUEST} when courseId is invalid")
    void getCourse_shouldReturnBadRequest_whenCourseIdIsInvalid() throws Exception {
        Long courseId = -1L;

        mockMvc.perform(get("/api/courses/{courseId}", courseId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getActiveCourseById(Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getPopularCourses should return pages")
    void getPopularCourses_shouldReturnPage() throws Exception {

        Page<CourseShortResponse> response =
                new PageImpl<>(List.of(
                        CourseTestData.courseShortResponse(1L, "Java"),
                        CourseTestData.courseShortResponse(2L, "Python")
                ));

        Mockito.when(service.getPopularCourses(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/courses/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java"));

        Mockito.verify(service).getPopularCourses(Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getPopularCourses should return empty page")
    void getPopularCourses_shouldReturnEmptyPage() throws Exception {

        Mockito.when(service.getPopularCourses(Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Endpoint getMyCourses should return pages")
    void getMyCourses_shouldReturnPage() throws Exception {

        Page<CourseShortResponse> response =
                new PageImpl<>(List.of(
                        CourseTestData.courseShortResponse(1L, "Java"),
                        CourseTestData.courseShortResponse(2L, "Python")
                ));

        Mockito.when(service.getMyCourse(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/courses/users/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java"));

        Mockito.verify(service).getMyCourse(Mockito.eq(1L), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getMyCourses should return empty page")
    void getMyCourses_shouldReturnEmptyPage() throws Exception {

        Mockito.when(service.getMyCourse(Mockito.eq(1L), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/users/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Endpoint getMyCourses should return {BAD_REQUEST} when userId is invalid")
    void getMyCourses_shouldReturnBadRequest_whenUserIdIsInvalid() throws Exception {
        Long userId = -1L;

        mockMvc.perform(get("/api/courses/users/{userId}/courses", userId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getActiveCourseById(Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getCoursesByStatus should return pages")
    void getCoursesByStatus_shouldReturnPage() throws Exception {

        Page<CourseShortResponse> response =
                new PageImpl<>(List.of(
                        CourseTestData.courseShortResponse(1L, "Java"),
                        CourseTestData.courseShortResponse(2L, "Python")
                ));

        Mockito.when(service.getCoursesByStatus(Mockito.eq(CourseStatus.PUBLISHED), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/courses")
                        .param("status", "PUBLISHED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java"));

        Mockito.verify(service)
                .getCoursesByStatus(Mockito.eq(CourseStatus.PUBLISHED), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getCoursesByStatus should work without status")
    void getCoursesByStatus_shouldWorkWithoutStatus() throws Exception {

        Mockito.when(service.getCoursesByStatus(Mockito.isNull(), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Endpoint getCoursesByTeacherId should return pages")
    void getCoursesByTeacherId_shouldReturnPage() throws Exception {

        Page<CourseShortResponse> response =
                new PageImpl<>(List.of(
                        CourseTestData.courseShortResponse(1L, "Java"),
                        CourseTestData.courseShortResponse(2L, "Python")
                ));

        Mockito.when(service.getCoursesByTeacherId(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/courses/teachers/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java"));

        Mockito.verify(service)
                .getCoursesByTeacherId(Mockito.eq(1L), Mockito.any());
    }

    @Test
    @DisplayName("Endpoint getCoursesByTeacherId should return empty page")
    void getCoursesByTeacherId_shouldReturnEmptyPage() throws Exception {

        Mockito.when(service.getCoursesByTeacherId(Mockito.eq(1L), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/teachers/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Endpoint getCoursesByTeacherId should return {BAD_REQUEST} when teacherId is invalid")
    void getCoursesByTeacherId_shouldReturnBadRequest_whenTeacherIdInvalid() throws Exception {
        Long teacherId = -1L;

        mockMvc.perform(get("/api/courses/teachers/{teacherId}/courses", teacherId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getActiveCourseById(Mockito.any());
    }

    @Test
    @DisplayName("Endpoint countAllCourses should return count of courses")
    void countAllCourses_shouldReturnCount() throws Exception {

        Mockito.when(service.countAllCourses())
                .thenReturn(5L);

        mockMvc.perform(get("/api/courses/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        Mockito.verify(service, Mockito.times(1)).countAllCourses();
    }

    @Test
    @DisplayName("Endpoint countTeacherCoursesById should return {BAD_REQUEST} when teacherId is invalid")
    void countTeacherCoursesById_shouldReturnBadRequest_whenTeacherIdInvalid() throws Exception {
        Long teacherId = -1L;

        mockMvc.perform(get("/api/courses//teachers/{teacherId}/count", teacherId))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).getActiveCourseById(Mockito.any());
    }

}
