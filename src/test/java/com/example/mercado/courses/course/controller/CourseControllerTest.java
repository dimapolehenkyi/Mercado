package com.example.mercado.courses.course.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.service.interfaces.CourseService;
import com.example.mercado.courses.testutils.course.CourseTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("Course Controller Test")
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseService service;

    @Test
    @DisplayName("Func createCourse should return response of created course")
    void createCourse_shouldReturnResponse() throws Exception {
        CreateCourseRequest request = CourseTestFactory.createTestCourseRequest();
        CourseResponse response = new CourseResponse(
                1L,
                1L,
                "Test",
                "Test description",
                BigDecimal.TEN,
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID,
                60,
                null,
                null
        );

        Mockito.when(service.createCourse(request)).thenReturn(response);

        mockMvc.perform(post("/api/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.status").value(CourseStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(CourseAccessType.PAID.name()))
                .andExpect(jsonPath("$.teacherId").value(1L))
                .andExpect(jsonPath("$.durationInMinutes").value(60));

        Mockito.verify(service, Mockito.times(1)).createCourse(request);
    }

    @Test
    @DisplayName("Func updateCourse should return response of updated course")
    void updateCourse_shouldReturnResponse() throws Exception {
        Long courseId = 1L;

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();

        CourseResponse response = new CourseResponse(
                1L,
                1L,
                "New name",
                "New description",
                BigDecimal.valueOf(100),
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID,
                60,
                null,
                null
        );

        Mockito.when(service.updateCourse(Mockito.eq(courseId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/api/courses/{courseId}", courseId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New name"))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(100)))
                .andExpect(jsonPath("$.status").value(CourseStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(CourseAccessType.PAID.name()))
                .andExpect(jsonPath("$.teacherId").value(1L))
                .andExpect(jsonPath("$.durationInMinutes").value(60));

        Mockito.verify(service, Mockito.times(1)).updateCourse(courseId, request);
    }

    @Test
    @DisplayName("Func deleteCourse should delete course from database")
    void deleteCourse_shouldDeleteCourse_fromDataBase() throws Exception {
        Long courseId = 1L;

        Mockito.doNothing()
                .when(service)
                .deleteCourse(Mockito.eq(courseId));

        mockMvc.perform(delete("/api/courses/{courseId}", courseId))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteCourse(courseId);
    }

    @Test
    @DisplayName("Func getAllCourses should return page of all courses")
    void getAllCourses_shouldReturnPage_ofAllCourses() throws Exception {
        CourseShortResponse course1 = new CourseShortResponse(
                1L,
                "Course 1",
                BigDecimal.valueOf(100),
                CourseAccessType.PAID,
                60,
                CourseStatus.PUBLISHED
        );

        CourseShortResponse course2 = new CourseShortResponse(
                2L,
                "Course 2",
                BigDecimal.valueOf(200),
                CourseAccessType.PAID,
                30,
                CourseStatus.DRAFT
        );

        List<CourseShortResponse> content = List.of(course1, course2);

        Page<CourseShortResponse> page = new PageImpl<>(
                content,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                content.size()
        );

        Mockito.when(service.getAllCourses(Mockito.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/courses")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.content.size()").value(2))

                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Course 1"))

                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Course 2"))

                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));

        Mockito.verify(service, Mockito.times(1)).getAllCourses(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Func getCourse should return response of course")
    void getCourse_shouldReturnResponse() throws Exception {
        Long courseId = 1L;

        CourseResponse response = new CourseResponse(
                1L,
                1L,
                "Test",
                "Test description",
                BigDecimal.TEN,
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID,
                60,
                null,
                null
        );

        Mockito.when(service.getCourseById(Mockito.eq(courseId))).thenReturn(response);

        mockMvc.perform(get("/api/courses/{courseId}", courseId))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.status").value(CourseStatus.PUBLISHED.name()))
                .andExpect(jsonPath("$.type").value(CourseAccessType.PAID.name()))
                .andExpect(jsonPath("$.teacherId").value(1L))
                .andExpect(jsonPath("$.durationInMinutes").value(60));

        Mockito.verify(service, Mockito.times(1)).getCourseById(courseId);
    }

    @Test
    @DisplayName("Func countAllCourses should return long of courses")
    void countAllCourses_shouldReturnLong() throws Exception {
        Long count = 5L;

        Mockito.when(service.countAllCourses()).thenReturn(count);

        mockMvc.perform(get("/api/courses/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(String.valueOf(count)));

        Mockito.verify(service, Mockito.times(1)).countAllCourses();
    }

    @Test
    @DisplayName("Func getPopularCourse should return list of popular courses")
    void getPopularCourse_shouldReturnList_ofPopularCourses() throws Exception {
        List<CourseShortResponse> courses = List.of(
                new CourseShortResponse(
                        1L,
                        "Course 1",
                        BigDecimal.valueOf(100),
                        CourseAccessType.PAID,
                        60,
                        CourseStatus.PUBLISHED),
                new CourseShortResponse(
                        2L,
                        "Course 2",
                        BigDecimal.valueOf(200),
                        CourseAccessType.PAID,
                        30,
                        CourseStatus.DRAFT)
        );

        Mockito.when(service.getPopularCourses())
                .thenReturn(courses);

        mockMvc.perform(get("/api/courses/popular"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(2))

                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Course 1"))

                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Course 2"));

        Mockito.verify(service).getPopularCourses();
    }

    @Test
    @DisplayName("Func getMyCourse should return list of my courses")
    void getMyCourse_shouldReturnList_ofMyCourses() throws Exception {
        Long userId = 1L;

        List<CourseShortResponse> courses = List.of(
                new CourseShortResponse(
                        1L,
                        "Course 1",
                        BigDecimal.valueOf(100),
                        CourseAccessType.PAID,
                        60,
                        CourseStatus.PUBLISHED),
                new CourseShortResponse(
                        2L,
                        "Course 2",
                        BigDecimal.valueOf(200),
                        CourseAccessType.PAID,
                        30,
                        CourseStatus.DRAFT)
        );

        Page<CourseShortResponse> page = new PageImpl<>(
                courses,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                courses.size()
        );

        Mockito.when(service.getMyCourse(
                Mockito.eq(userId),
                Mockito.any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/courses/my/{userId}", userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.content.size()").value(2))

                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Course 1"))

                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Course 2"))

                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        Mockito.verify(service).getMyCourse(
                Mockito.eq(userId),
                Mockito.any(Pageable.class)
        );
    }

    @Test
    @DisplayName("Func getArchivedCourse should return page of archived courses")
    void getArchivedCourse_shouldReturnPage_ofArchivedCourses() throws Exception {
        List<CourseShortResponse> courses = List.of(
                new CourseShortResponse(
                        1L,
                        "Archived 1",
                        BigDecimal.valueOf(100),
                        CourseAccessType.PAID,
                        60,
                        CourseStatus.ARCHIVED),
                new CourseShortResponse(
                        2L,
                        "Archived 2",
                        BigDecimal.valueOf(200),
                        CourseAccessType.PAID,
                        30,
                        CourseStatus.ARCHIVED)
        );

        Page<CourseShortResponse> page = new PageImpl<>(
                courses,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                courses.size()
        );

        Mockito.when(service.getArchivedCourse(Mockito.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/courses/status/archived")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Archived 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Archived 2"))

                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        Mockito.verify(service).getArchivedCourse(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Func getPublishedCourse should return page of published courses")
    void getPublishedCourse_shouldReturnPage_ofPublishedCourses() throws Exception {
        List<CourseShortResponse> courses = List.of(
                new CourseShortResponse(
                        1L,
                        "Published 1",
                        BigDecimal.valueOf(100),
                        CourseAccessType.PAID,
                        60,
                        CourseStatus.PUBLISHED),
                new CourseShortResponse(
                        2L,
                        "Published 2",
                        BigDecimal.valueOf(200),
                        CourseAccessType.PAID,
                        30,
                        CourseStatus.PUBLISHED)
        );

        Page<CourseShortResponse> page = new PageImpl<>(
                courses,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                courses.size()
        );

        Mockito.when(service.getPublishedCourse(Mockito.any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/courses/status/published")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Published 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Published 2"))

                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        Mockito.verify(service).getPublishedCourse(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Func searchCourse should return page of searched courses")
    void searchCourse_shouldReturnPage_ofSearchedCourses() throws Exception {
        CourseSearchFilter filter = new CourseSearchFilter(
                "Java",
                CourseAccessType.PAID,
                1L,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(300)
        );

        List<CourseShortResponse> courses = List.of(
                new CourseShortResponse(
                        1L, "Java 1", BigDecimal.valueOf(100),
                        CourseAccessType.PAID, 60, CourseStatus.PUBLISHED
                ),
                new CourseShortResponse(
                        2L, "Java 2", BigDecimal.valueOf(200),
                        CourseAccessType.PAID, 30, CourseStatus.PUBLISHED
                )
        );

        Page<CourseShortResponse> page = new PageImpl<>(
                courses,
                PageRequest.of(0, 10, Sort.by("createdAt")),
                courses.size()
        );

        Mockito.when(service.searchCourse(
                Mockito.any(CourseSearchFilter.class),
                Mockito.any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/courses/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filter))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java 1"))
                .andExpect(jsonPath("$.content[1].name").value("Java 2"))

                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        Mockito.verify(service).searchCourse(
                Mockito.any(CourseSearchFilter.class),
                Mockito.any(Pageable.class)
        );
    }

    @Test
    @DisplayName("Func getCoursesByTeacher should return page of teacher's course")
    void getCoursesByTeacher_shouldReturnPage_ofTeachersCourse() throws Exception {
        Long teacherId = 1L;

        List<CourseShortResponse> courses = List.of(
                new CourseShortResponse(
                        1L, "Course 1", BigDecimal.valueOf(100),
                        CourseAccessType.PAID, 60, CourseStatus.PUBLISHED
                ),
                new CourseShortResponse(
                        2L, "Course 2", BigDecimal.valueOf(200),
                        CourseAccessType.PAID, 30, CourseStatus.DRAFT
                )
        );

        Page<CourseShortResponse> page = new PageImpl<>(
                courses,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                courses.size()
        );

        Mockito.when(service.getCourseByTeacher(
                Mockito.eq(teacherId),
                Mockito.any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/courses/teacher/{teacherId}", teacherId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Course 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Course 2"))

                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        Mockito.verify(service).getCourseByTeacher(
                Mockito.eq(teacherId),
                Mockito.any(Pageable.class)
        );
    }
}
