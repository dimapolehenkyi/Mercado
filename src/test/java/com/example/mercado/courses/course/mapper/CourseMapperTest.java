package com.example.mercado.courses.course.mapper;

import com.example.mercado.courses.course.dto.CourseResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.testutils.course.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("CourseMapper Test")
public class CourseMapperTest {

    private CourseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CourseMapper();
    }

    @Test
    @DisplayName("Func toEntity should map all fields to entity type")
    void toEntity_shouldMapAllFields() {
        Long teacherId = 1L;

        CreateCourseRequest request = CourseTestFactory.createTestCourseRequest();

        Course mapped = mapper.toEntity(request);

        Assertions.assertNotNull(mapped);
        Assertions.assertEquals(teacherId, mapped.getTeacherId());
        Assertions.assertEquals("Test", mapped.getName());
        Assertions.assertEquals("Test description", mapped.getDescription());
        Assertions.assertEquals(CourseAccessType.PAID, mapped.getType());
        Assertions.assertEquals(CourseStatus.PUBLISHED, mapped.getStatus());
    }

    @Test
    @DisplayName("Func updateEntity should map fields to new value")
    void updateEntity_shouldMapFields_toNewValue() {
        Long userId = 1L;
        Long teacherId = 1L;
        String name = "Java Core Course";

        Course course = CourseTestFactory.createTestCourse(userId, teacherId, name);

        UpdateCourseRequest request = CourseTestFactory.updateCourseRequest();

        mapper.updateEntity(course, request);


        Assertions.assertEquals("New name", course.getName());
        Assertions.assertEquals("New description", course.getDescription());
        Assertions.assertEquals(CourseStatus.PUBLISHED, course.getStatus());
        Assertions.assertEquals(BigDecimal.valueOf(100), course.getPrice());
        Assertions.assertEquals(CourseAccessType.PAID, course.getType());
    }

    @Test
    @DisplayName("Func toResponse should map all fields to response type")
    void toResponse_shouldMapAllFields() {
        Long userId = 1L;
        Long teacherId = 1L;
        String name = "Java Core Course";

        Course course = CourseTestFactory.createTestCourse(userId, teacherId, name);

        CourseResponse mapped = mapper.toResponse(course);

        Assertions.assertNotNull(mapped);
        Assertions.assertEquals(teacherId, mapped.teacherId());
        Assertions.assertEquals(name, mapped.name());
        Assertions.assertEquals("description", mapped.description());
        Assertions.assertEquals(BigDecimal.ZERO, mapped.price());
        Assertions.assertEquals(CourseStatus.PUBLISHED, mapped.status());
        Assertions.assertEquals(CourseAccessType.FREE, mapped.type());
        Assertions.assertEquals(0, mapped.durationInMinutes());
    }

    @Test
    @DisplayName("Func toShortResponse should map all fields to short response type")
    void toShortResponse_shouldMapAllFields() {
        Long userId = 1L;
        Long teacherId = 1L;
        String name = "Java Core Course";

        Course course = CourseTestFactory.createTestCourse(userId, teacherId, name);

        CourseShortResponse mapped = mapper.toShortResponse(course);

        Assertions.assertNotNull(mapped);
        Assertions.assertEquals(name, mapped.name());
        Assertions.assertEquals(BigDecimal.ZERO, mapped.price());
        Assertions.assertEquals(CourseStatus.PUBLISHED, mapped.status());
        Assertions.assertEquals(CourseAccessType.FREE, mapped.type());
        Assertions.assertEquals(0, mapped.durationInMinutes());
    }
}
