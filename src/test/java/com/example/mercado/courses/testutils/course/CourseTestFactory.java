package com.example.mercado.courses.testutils.course;

import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;

import java.math.BigDecimal;

public class CourseTestFactory {

    public static Course createTestCourse(
            Long userId, Long teacherId, String name
    ) {
        Course course = new Course();
        course.setUserId(userId);
        course.setName(name);
        course.setTeacherId(teacherId);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setDescription("description");
        course.setPrice(BigDecimal.ZERO);
        course.setType(CourseAccessType.FREE);
        course.setDurationInMinutes(0);
        return course;
    }

    public static Course createTestCourseDraft(
            Long userId, Long teacherId, String name
    ) {
        Course course = new Course();
        course.setUserId(userId);
        course.setName(name);
        course.setTeacherId(teacherId);
        course.setStatus(CourseStatus.DRAFT);
        course.setDescription("description");
        course.setPrice(BigDecimal.ZERO);
        course.setType(CourseAccessType.FREE);
        course.setDurationInMinutes(0);
        return course;
    }

    public static CreateCourseRequest createTestCourseRequest() {
        return new CreateCourseRequest(
                1L,
                "Test",
                "Test description",
                CourseAccessType.PAID,
                BigDecimal.TEN,
                60,
                CourseStatus.PUBLISHED
        );
    }

        public static CreateCourseRequest createTestCourseRequestFree() {
        return new CreateCourseRequest(
                1L,
                "Test",
                "Test description",
                CourseAccessType.FREE,
                BigDecimal.ZERO,
                60,
                CourseStatus.PUBLISHED
        );
    }


    public static UpdateCourseRequest updateCourseRequest() {
        return new UpdateCourseRequest(
                "New name",
                "New description",
                BigDecimal.valueOf(100),
                CourseStatus.PUBLISHED,
                CourseAccessType.PAID
        );
    }

}
