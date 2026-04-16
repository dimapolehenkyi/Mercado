package com.example.mercado.courses.course.courseLearningPoint.mapper;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("CourseLearningPointMapper Test")
@ExtendWith(SpringExtension.class)
@Import(CourseLearningPointMapperImpl.class)
public class CourseLearningPointMapperTest {

    @Mock
    private final CourseLearningPointMapper mapper = Mappers.getMapper(CourseLearningPointMapper.class);



    @Test
    @DisplayName("toEntity should map request correctly")
    void toEntity_shouldMapCorrectly() {
        AddLearningPointRequest request = new AddLearningPointRequest("text");

        CourseLearningPoint entity = mapper.toEntity(1L, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals("text", entity.getText()),
                () -> Assertions.assertEquals(1L, entity.getCourseId())
        );
    }

    @Test
    @DisplayName("updateEntity should update fields correctly")
    void updateEntity_shouldUpdateFields() {
        CourseLearningPoint entity = CourseLearningPoint.builder()
                .text("old text")
                .position(1)
                .build();

        UpdateLearningPointRequest request = new UpdateLearningPointRequest("new text");

        mapper.updateEntity(entity, request);

        Assertions.assertEquals("new text", entity.getText());
    }

    @Test
    @DisplayName("updateEntity should ignore null values")
    void updateEntity_shouldIgnoreNulls() {
        CourseLearningPoint entity = CourseLearningPoint.builder()
                .text("old text")
                .position(1)
                .build();

        UpdateLearningPointRequest request = new UpdateLearningPointRequest(null);

        mapper.updateEntity(entity, request);

        Assertions.assertEquals("old text", entity.getText());
    }

    @Test
    @DisplayName("Func toResponse should map real values correctly")
    void toResponse_shouldMapRealValues() {
        CourseLearningPoint point = CourseLearningPoint.builder()
                .id(1L)
                .courseId(1L)
                .text("text")
                .position(1)
                .build();

        LearningPointResponse response = mapper.toResponse(point);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("text", response.text()),
                () -> Assertions.assertEquals(1, response.position())
        );
    }

    @Test
    @DisplayName("mapper should not crash on completely empty entity")
    void shouldNotCrash_onEmptyEntity() {
        CourseLearningPoint point = new CourseLearningPoint();

        Assertions.assertDoesNotThrow(() -> {
            mapper.toResponse(point);
        });
    }


}
