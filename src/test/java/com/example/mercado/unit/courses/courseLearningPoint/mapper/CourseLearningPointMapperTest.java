package com.example.mercado.unit.courses.courseLearningPoint.mapper;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapper;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapperImpl;
import com.example.mercado.testUtils.courses.courseLearningPoint.CourseLPTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("CourseLearningPoint Mapper Test")
@ExtendWith(SpringExtension.class)
@Import(CourseLearningPointMapperImpl.class)
public class CourseLearningPointMapperTest {

    @Mock
    private final CourseLearningPointMapper mapper = Mappers.getMapper(CourseLearningPointMapper.class);

    @Test
    @DisplayName("toResponse should map numeric fields with default values when null")
    void toResponse_shouldMapDefaultValues_whenNumericFieldsAreNull() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(null)
                .courseId(null)
                .text(null)
                .position(null)
                .build();

        LearningPointResponse response = mapper.toResponse(point);

        Assertions.assertAll(
                () -> Assertions.assertNull(response.id()),
                () -> Assertions.assertNull(response.courseId()),
                () -> Assertions.assertNull(response.text()),
                () -> Assertions.assertNull(response.position())
        );
    }

    @Test
    @DisplayName("toResponse should map all basic fields correctly")
    void toResponse_shouldMapAllFieldsCorrectly() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        LearningPointResponse response = mapper.toResponse(point);

        Assertions.assertAll(
                () -> Assertions.assertEquals(response.id(), point.getId()),
                () -> Assertions.assertEquals(response.courseId(), point.getCourseId()),
                () -> Assertions.assertEquals(response.text(), point.getText()),
                () -> Assertions.assertEquals(response.position(), point.getPosition())
        );
    }

    @Test
    @DisplayName("toResponse should not throw when entity is empty")
    void toResponse_shouldNotThrow_whenEntityIsEmpty() {
        CourseLearningPoint point = new CourseLearningPoint();

        Assertions.assertDoesNotThrow(
                () -> mapper.toResponse(point)
        );
    }

    @Test
    @DisplayName("toEntity should map text and courseId correctly")
    void toEntity_shouldMapFieldsCorrectly() {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                "Test"
        );

        CourseLearningPoint point = mapper.toEntity(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(point),
                () -> Assertions.assertEquals(request.text(), point.getText()),
                () -> Assertions.assertEquals(courseId, point.getCourseId())
        );
    }

    @Test
    @DisplayName("toEntity should map null text when request text is null")
    void toEntity_shouldMapNullText_whenRequestTextIsNull() {
        Long courseId = 1L;

        CourseLearningPoint point = mapper.toEntity(courseId, null);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(point),
                () -> Assertions.assertNull(point.getText()),
                () -> Assertions.assertEquals(courseId, point.getCourseId())
        );
    }

    @Test
    @DisplayName("toEntity should map blank text as is")
    void toEntity_shouldMapBlankText() {
        Long courseId = 1L;
        AddLearningPointRequest request = new AddLearningPointRequest(
                ""
        );

        CourseLearningPoint point = mapper.toEntity(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(point),
                () -> Assertions.assertEquals(request.text(), point.getText()),
                () -> Assertions.assertEquals(courseId, point.getCourseId())
        );
    }

    @Test
    @DisplayName("toEntity should not throw when request is empty")
    void toEntity_shouldNotThrow_whenRequestIsEmpty() {
        Long courseId = 1L;

        Assertions.assertDoesNotThrow(
                () -> mapper.toEntity(courseId, null)
        );
    }

    @Test
    @DisplayName("updateEntity should update text when provided")
    void updateEntity_shouldUpdateText_whenProvided() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                "Updated"
        );

        mapper.updateEntity(point, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, point.getId()),
                () -> Assertions.assertEquals(1L, point.getCourseId()),
                () -> Assertions.assertEquals("Updated", point.getText()),
                () -> Assertions.assertEquals(1, point.getPosition())
        );
    }

    @Test
    @DisplayName("updateEntity should not update text when request text is null")
    void updateEntity_shouldNotUpdateText_whenNull() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(
                null
        );

        mapper.updateEntity(point, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, point.getId()),
                () -> Assertions.assertEquals(1L, point.getCourseId()),
                () -> Assertions.assertEquals("Test text", point.getText()),
                () -> Assertions.assertEquals(1, point.getPosition())
        );
    }

    @Test
    @DisplayName("updateEntity should keep existing values when request is empty")
    void updateEntity_shouldKeepExistingValues_whenRequestIsEmpty() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        mapper.updateEntity(point, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, point.getId()),
                () -> Assertions.assertEquals(1L, point.getCourseId()),
                () -> Assertions.assertEquals("Test text", point.getText()),
                () -> Assertions.assertEquals(1, point.getPosition())
        );
    }

    @Test
    @DisplayName("updateEntity should not throw when entity has null fields")
    void updateEntity_shouldNotThrow_whenEntityHasNullFields() {
        CourseLearningPoint point = CourseLPTestFactory.createDefaultCourseLP()
                .id(1L)
                .build();

        Assertions.assertDoesNotThrow(
                () -> mapper.updateEntity(point, null)
        );
    }
}
