package com.example.mercado.courses.course.controller;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.service.interfaces.CourseQueryService;
import com.example.mercado.courses.testutils.course.CourseTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private CourseQueryService service;


    @Test
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
    void getCourse_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(service.getActiveCourseById(1L))
                .thenThrow(new AppException(ErrorCode.COURSE_NOT_FOUND));

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
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
    }

    @Test
    void getPopularCourses_shouldReturnEmptyPage() throws Exception {

        Mockito.when(service.getPopularCourses(Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getPopularCourses_shouldPassPageable() throws Exception {

        Mockito.when(service.getPopularCourses(Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/popular")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "studentCount,desc"))
                .andExpect(status().isOk());

        Mockito.verify(service).getPopularCourses(Mockito.any());
    }

    @Test
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
    }

    @Test
    void getMyCourses_shouldReturnEmptyPage() throws Exception {

        Mockito.when(service.getMyCourse(Mockito.eq(1L), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/users/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getMyCourses_shouldPassUserIdToService() throws Exception {

        Mockito.when(service.getMyCourse(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/users/42/courses"))
                .andExpect(status().isOk());

        Mockito.verify(service).getMyCourse(Mockito.eq(42L), Mockito.any());
    }

    @Test
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
    }

    @Test
    void getCoursesByStatus_shouldWorkWithoutStatus() throws Exception {

        Mockito.when(service.getCoursesByStatus(Mockito.isNull(), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getCoursesByStatus_shouldPassStatusToService() throws Exception {

        Mockito.when(service.getCoursesByStatus(Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses")
                        .param("status", "DRAFT"))
                .andExpect(status().isOk());

        Mockito.verify(service)
                .getCoursesByStatus(Mockito.eq(CourseStatus.DRAFT), Mockito.any());
    }

    @Test
    void getCoursesByTeacher_shouldReturnPage() throws Exception {

        Page<CourseShortResponse> response =
                new PageImpl<>(List.of(
                        CourseTestData.courseShortResponse(1L, "Java"),
                        CourseTestData.courseShortResponse(2L, "Python")
                ));

        Mockito.when(service.getCoursesByTeacher(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/courses/teachers/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java"));
    }

    @Test
    void getCoursesByTeacher_shouldReturnEmptyPage() throws Exception {

        Mockito.when(service.getCoursesByTeacher(Mockito.eq(1L), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/teachers/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getCoursesByTeacher_shouldPassTeacherIdToService() throws Exception {

        Mockito.when(service.getCoursesByTeacher(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/courses/teachers/42/courses"))
                .andExpect(status().isOk());

        Mockito.verify(service)
                .getCoursesByTeacher(Mockito.eq(42L), Mockito.any());
    }

    @Test
    void countAllCourses_shouldReturnCount() throws Exception {

        Mockito.when(service.countAllCourses())
                .thenReturn(5L);

        mockMvc.perform(get("/api/courses/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void countAllCourses_shouldCallService() throws Exception {

        Mockito.when(service.countAllCourses())
                .thenReturn(0L);

        mockMvc.perform(get("/api/courses/count"))
                .andExpect(status().isOk());

        Mockito.verify(service).countAllCourses();
    }

}
