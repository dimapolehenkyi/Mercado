package com.example.mercado.unit.courses.course.mapper;

import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.mapper.CourseMapperImpl;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;


@DisplayName("CourseMapper Test")
@ExtendWith(SpringExtension.class)
@Import(CourseMapperImpl.class)
public class CourseMapperTest {

    @Mock
    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);


    @Test
    @DisplayName("toResponse should apply default values for null numeric fields")
    void toResponse_shouldApplyDefaults_whenNullValues() {
        Course course = new Course();

        CourseDetailsResponse response = mapper.toResponse(course);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, response.studentCount()),
                () -> Assertions.assertEquals(0.0, response.rating()),
                () -> Assertions.assertEquals(0L, response.reviewsCount()),
                () -> Assertions.assertEquals(0, response.durationInMinutes())
        );
    }

    @Test
    @DisplayName("toResponse should map real values correctly")
    void toResponse_shouldMapRealValues() {
        Course course = CourseTestFactory.createCourse(a -> {
                    a.studentCount(10L);
                    a.rating(4.5);
                    a.reviewsCount(3L);
                    a.durationInMinutes(120);
                }
        );

        CourseDetailsResponse response = mapper.toResponse(course);

        Assertions.assertAll(
                () -> Assertions.assertEquals(10L, response.studentCount()),
                () -> Assertions.assertEquals(4.5, response.rating()),
                () -> Assertions.assertEquals(3L, response.reviewsCount()),
                () -> Assertions.assertEquals(120, response.durationInMinutes())
        );
    }

    @Test
    @DisplayName("toShortResponse should default rating to 0.0 when null")
    void toShortResponse_shouldDefaultRating() {
        Course course = CourseTestFactory.createCourse(a -> {
                    a.name("Java");
                    a.rating(null);
                }
        );

        CourseShortResponse response = mapper.toShortResponse(course);

        Assertions.assertEquals(0.0, response.rating());
    }

    @Test
    @DisplayName("toShortResponse should map fields correctly")
    void toShortResponse_shouldMapCorrectly() {
        Course course = CourseTestFactory.createCourse(a -> {
                    a.name("Java");
                    a.price(BigDecimal.valueOf(100L));
                    a.type(CourseAccessType.PAID);
                    a.status(CourseStatus.PUBLISHED);
                    a.rating(4.2);
                }
        );

        CourseShortResponse response = mapper.toShortResponse(course);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Java", response.name()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(100L), response.price()),
                () -> Assertions.assertEquals(CourseAccessType.PAID, response.type()),
                () -> Assertions.assertEquals(CourseStatus.PUBLISHED, response.status()),
                () -> Assertions.assertEquals(4.2, response.rating())
        );
    }

    @Test
    @DisplayName("mapper should not crash on completely empty entity")
    void shouldNotCrash_onEmptyEntity() {
        Course course = new Course();

        Assertions.assertDoesNotThrow(() -> {
            mapper.toResponse(course);
            mapper.toShortResponse(course);
        });
    }
}
