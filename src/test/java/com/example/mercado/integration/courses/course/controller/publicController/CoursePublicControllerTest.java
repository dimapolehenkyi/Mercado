package com.example.mercado.integration.courses.course.controller.publicController;

import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CoursePublicController Integration Test")
public class CoursePublicControllerTest extends AbstractRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository repository;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    @Test
    void getCourse_shouldReturnCourse_whenCorrectRequest() throws Exception {
        Course course = repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.level(CourseLevel.INTERMEDIATE);
                        }
                )
        );

        Course saved = repository.findActiveById(course.getId()).get();

        mockMvc.perform(get("/api/courses/{courseId}",  saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(saved.getName()))
                .andExpect(jsonPath("$.level").value(saved.getLevel().name()))
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void getCourse_shouldThrowException_whenCourseNotFound() throws Exception {
        Long fakeCourseId = 1L;

        mockMvc.perform(get("/api/courses/{courseId}",  fakeCourseId))
                .andExpect(status().isNotFound());
    }


    @Test
    void getPopularCourse_shouldReturnCoursePages_whenCorrectRequest() throws Exception {
        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.INTERMEDIATE);
                            a.studentCount(10L);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.BEGINNER);
                            a.studentCount(30L);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Python");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.ADVANCED);
                            a.studentCount(8L);
                        }
                )
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/popular")
                        .param("page", "0")
                        .param("size", "3")
                )
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("C++"))
                .andExpect(jsonPath("$.content[1].name").value("Java"))
                .andExpect(jsonPath("$.content[2].name").value("Python"));
    }


    @Test
    void getMyCourse_shouldReturnCoursePages_whenCorrectRequest() throws Exception {
        Long userId = 1L;

        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.INTERMEDIATE);
                            a.userId(userId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.BEGINNER);
                            a.userId(userId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Python");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.ADVANCED);
                            a.userId(userId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C#");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.ADVANCED);
                            a.userId(2L);
                        }
                )
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/users/{userId}/courses", userId)
                        .param("page", "0")
                        .param("size", "5")
                )
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("Python"))
                .andExpect(jsonPath("$.content[1].name").value("C++"))
                .andExpect(jsonPath("$.content[2].name").value("Java"));
    }


    @Test
    void searchCourse_shouldReturnCoursesWhenFilterMatches() throws Exception {
        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.INTERMEDIATE);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.BEGINNER);
                        }
                )
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/search")
                        .param("page", "0")
                        .param("size", "5")
                        .param("keyword", "Java")
                        .param("level", "INTERMEDIATE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Java"));
    }

    @Test
    void searchCourse_shouldReturnCoursesSortedByCreatedAtAscByDefault() throws Exception {
        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.INTERMEDIATE);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.INTERMEDIATE);
                        }
                )
        );

        ReflectionTestUtils.setField(
                courses.get(0),
                "createdAt",
                LocalDateTime.now().minusDays(1)
        );

        ReflectionTestUtils.setField(
                courses.get(1),
                "createdAt",
                LocalDateTime.now()
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/search")
                        .param("page", "0")
                        .param("size", "5")
                        .param("level", "INTERMEDIATE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Java"))
                .andExpect(jsonPath("$.content[1].name").value("C++"));
    }


    @Test
    void getCoursesByTeacherId_shouldReturnCoursePages_whenCorrectRequest() throws Exception {
        Long teacherId = 1L;

        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.INTERMEDIATE);
                            a.teacherId(teacherId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.BEGINNER);
                            a.teacherId(teacherId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Python");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.ADVANCED);
                            a.teacherId(teacherId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C#");
                            a.status(CourseStatus.PUBLISHED);
                            a.level(CourseLevel.ADVANCED);
                            a.teacherId(2L);
                        }
                )
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/teachers/{teacherId}/courses", teacherId)
                        .param("page", "0")
                        .param("size", "5")
                )
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("Python"))
                .andExpect(jsonPath("$.content[1].name").value("C++"))
                .andExpect(jsonPath("$.content[2].name").value("Java"));
    }


    @Test
    void countAllCourses_shouldReturnNumberOfCourses_whenCoursesExists() throws Exception {
        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Python");
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C#");
                        }
                )
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    void countAllCourses_shouldReturnZero_whenCoursesDoesntExists() throws Exception {
        mockMvc.perform(get("/api/courses/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }


    @Test
    void countTeacherCoursesById_shouldReturnNumberOfTeacherCourses_whenTeacherCoursesExists() throws Exception {
        Long teacherId = 1L;

        List<Course> courses = List.of(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Java");
                            a.teacherId(teacherId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C++");
                            a.teacherId(teacherId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Python");
                            a.teacherId(teacherId);
                        }
                ),
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("C#");
                            a.teacherId(2L);
                        }
                )
        );

        repository.saveAll(courses);

        mockMvc.perform(get("/api/courses/teachers/{teacherId}/count", teacherId))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void countTeacherCoursesById_shouldReturnZero_whenTeacherCoursesDoesntExists() throws Exception {
        Long teacherId = 1L;

        mockMvc.perform(get("/api/courses/teachers/{teacherId}/count", teacherId))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }


    /**
     * Ensures that endpoints with negative path variables return HTTP 400 Bad Request.
     */
    @ParameterizedTest
    @MethodSource("negativeIdEndpoints")
    void shouldReturnBadRequestWhenIdIsNegative(
            String url
    ) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> negativeIdEndpoints() {
        return Stream.of(
                "/api/courses/-1",
                "/api/courses/users/-1/courses",
                "/api/courses/teachers/-1/courses",
                "/api/courses/teachers/-1/count"
        );
    }


    /**
     * Verifies that list endpoints return an empty page when no data is available.
     */
    @ParameterizedTest
    @MethodSource("emptyResultOfEndpoints")
    void shouldReturnEmptyPageWhenNoneExists(
            String url
    ) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    static Stream<String> emptyResultOfEndpoints() {
        return Stream.of(
                "/api/courses/popular",
                "/api/courses/users/1/courses",
                "/api/courses/teachers/1/courses",
                "/api/courses/search"
        );
    }


    /**
     * Verifies that course search filters (keyword and level) are applied correctly and return expected number of results.
     */
    @ParameterizedTest
    @MethodSource("resultOfApplyingFilters")
    void shouldApplyFilters (
            String query,
            int expectedCount
    ) throws Exception {
        repository.saveAll(
                List.of(
                        CourseTestFactory.createCourse(
                                a -> {
                                    a.name("java");
                                    a.level(CourseLevel.INTERMEDIATE);
                                    a.status(CourseStatus.PUBLISHED);
                                    a.type(CourseAccessType.FREE);
                                }
                        ),
                        CourseTestFactory.createCourse(
                                a -> {
                                    a.name("c#");
                                    a.level(CourseLevel.ADVANCED);
                                    a.status(CourseStatus.PUBLISHED);
                                    a.type(CourseAccessType.PAID);
                                }
                        ),
                        CourseTestFactory.createCourse(
                                a -> {
                                    a.name("python");
                                    a.level(CourseLevel.BEGINNER);
                                    a.status(CourseStatus.PUBLISHED);
                                    a.type(CourseAccessType.FREE);
                                }
                        ),
                        CourseTestFactory.createCourse(
                                a -> {
                                    a.name("c++");
                                    a.level(CourseLevel.ADVANCED);
                                    a.status(CourseStatus.PUBLISHED);
                                    a.type(CourseAccessType.PAID);
                                }
                        )
                )
        );

        mockMvc.perform(get("/api/courses/search" + query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedCount));
    }

    static Stream<Arguments> resultOfApplyingFilters() {
        return Stream.of(
                Arguments.of("?keyword=java", 1),
                Arguments.of("?level=ADVANCED", 2),
                Arguments.of("?status=PUBLISHED", 4),
                Arguments.of("?type=PAID", 2),
                Arguments.of("?type=FREE", 2),
                Arguments.of("?keyword=python&level=BEGINNER", 1),
                Arguments.of("?status=PUBLISHED&type=FREE", 2),
                Arguments.of("?status=PUBLISHED&type=PAID", 2),
                Arguments.of("?keyword=nonexistent", 0)
        );
    }


    @ParameterizedTest
    @MethodSource("resultOfBadRequest")
    void shouldReturnBadRequestWhenFilterValidationFails(String query) throws Exception {
        mockMvc.perform(get("/api/courses/search" + query))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> resultOfBadRequest() {
        return Stream.of(
                "?status=NoneExists",
                "?level=NoneExists",
                "?type=NoneExists",
                "?priceFrom=40&priceTo=30",
                "?priceFrom=-40",
                "?priceTo=-30",
                "?priceFrom=ABC",
                "?priceTo=ABC",
                "?keyword=__??}{`;."
        );
    }

}
