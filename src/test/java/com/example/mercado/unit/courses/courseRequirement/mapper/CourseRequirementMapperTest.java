package com.example.mercado.unit.courses.courseRequirement.mapper;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapper;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapperImpl;
import com.example.mercado.testUtils.courses.courseLearningPoint.CourseLPTestFactory;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestFactory;
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
@Import(CourseRequirementMapperImpl.class)
public class CourseRequirementMapperTest {

    @Mock
    private final CourseRequirementMapper mapper = Mappers.getMapper(CourseRequirementMapper.class);

    @Test
    @DisplayName("toResponse should map numeric fields with default values when null")
    void toResponse_shouldMapDefaultValues_whenNumericFieldsAreNull() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(null)
                .courseId(null)
                .text(null)
                .position(null)
                .build();

        RequirementResponse response = mapper.toResponse(requirement);

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
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        RequirementResponse response = mapper.toResponse(requirement);

        Assertions.assertAll(
                () -> Assertions.assertEquals(response.id(), requirement.getId()),
                () -> Assertions.assertEquals(response.courseId(), requirement.getCourseId()),
                () -> Assertions.assertEquals(response.text(), requirement.getText()),
                () -> Assertions.assertEquals(response.position(), requirement.getPosition())
        );
    }

    @Test
    @DisplayName("toResponse should not throw when entity is empty")
    void toResponse_shouldNotThrow_whenEntityIsEmpty() {
        CourseRequirement requirement = new CourseRequirement();

        Assertions.assertDoesNotThrow(
                () -> mapper.toResponse(requirement)
        );
    }

    @Test
    @DisplayName("toEntity should map text and courseId correctly")
    void toEntity_shouldMapFieldsCorrectly() {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                "Test"
        );

        CourseRequirement point = mapper.toEntity(courseId, request);

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

        CourseRequirement requirement = mapper.toEntity(courseId, null);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(requirement),
                () -> Assertions.assertNull(requirement.getText()),
                () -> Assertions.assertEquals(courseId, requirement.getCourseId())
        );
    }

    @Test
    @DisplayName("toEntity should map blank text as is")
    void toEntity_shouldMapBlankText() {
        Long courseId = 1L;
        AddRequirementRequest request = new AddRequirementRequest(
                ""
        );

        CourseRequirement point = mapper.toEntity(courseId, request);

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
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Updated"
        );

        mapper.updateEntity(requirement, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, requirement.getId()),
                () -> Assertions.assertEquals(1L, requirement.getCourseId()),
                () -> Assertions.assertEquals("Updated", requirement.getText()),
                () -> Assertions.assertEquals(1, requirement.getPosition())
        );
    }

    @Test
    @DisplayName("updateEntity should not update text when request text is null")
    void updateEntity_shouldNotUpdateText_whenNull() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                null
        );

        mapper.updateEntity(requirement, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, requirement.getId()),
                () -> Assertions.assertEquals(1L, requirement.getCourseId()),
                () -> Assertions.assertEquals("Test text", requirement.getText()),
                () -> Assertions.assertEquals(1, requirement.getPosition())
        );
    }

    @Test
    @DisplayName("updateEntity should keep existing values when request is empty")
    void updateEntity_shouldKeepExistingValues_whenRequestIsEmpty() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        mapper.updateEntity(requirement, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, requirement.getId()),
                () -> Assertions.assertEquals(1L, requirement.getCourseId()),
                () -> Assertions.assertEquals("Test text", requirement.getText()),
                () -> Assertions.assertEquals(1, requirement.getPosition())
        );
    }

    @Test
    @DisplayName("updateEntity should not throw when entity has null fields")
    void updateEntity_shouldNotThrow_whenEntityHasNullFields() {
        CourseRequirement requirement = CourseRequirementTestFactory.createDefaultCourseRequirement()
                .id(1L)
                .build();

        Assertions.assertDoesNotThrow(
                () -> mapper.updateEntity(requirement, null)
        );
    }

}
