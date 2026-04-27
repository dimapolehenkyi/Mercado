package com.example.mercado.integration.courses.courseRequirement.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.adminService.CourseAdminRequirementService;
import com.example.mercado.testUtils.courses.courseRequirement.CourseRequirementTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CourseAdminRequirementServiceImplTest {

    @Autowired
    private CourseRequirementRepository repository;

    @Autowired
    private CourseAdminRequirementService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }



    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourseRequirement should set correct last position when some requirements already exists")
    void createCourseRequirement_shouldSetCorrectLastPosition_whenSomeRequirementsAlreadyExists() {
        Long courseId = 1L;

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );

        AddRequirementRequest request = new AddRequirementRequest(
                "New point"
        );

        RequirementResponse response = service.createCourseRequirement(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(4L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("New point", response.text()),
                () -> Assertions.assertEquals(4, response.position())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("createCourseRequirement should set correct first position when no one requirements exists")
    void createCourseRequirement_shouldSetCorrectFirstPosition_whenNoOneRequirementsExists() {
        Long courseId = 1L;

        AddRequirementRequest request = new AddRequirementRequest(
                "New point"
        );

        RequirementResponse response = service.createCourseRequirement(courseId, request);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1L, response.id()),
                () -> Assertions.assertEquals(1L, response.courseId()),
                () -> Assertions.assertEquals("New point", response.text()),
                () -> Assertions.assertEquals(0, response.position())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseRequirement should update text if request correct")
    void updateCourseRequirement_shouldUpdateText_ifRequestCorrect() {
        Long courseId = 1L;

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "New text"
        );

        RequirementResponse response = service.updateCourseRequirement(
                requirement.getId(),
                courseId,
                request
        );

        CourseRequirement updated = repository.findById(requirement.getId()).orElseThrow();

        Assertions.assertAll(
                () -> Assertions.assertEquals(response.id(), updated.getId()),
                () -> Assertions.assertEquals(response.courseId(), updated.getCourseId()),
                () -> Assertions.assertEquals(response.text(), updated.getText()),
                () -> Assertions.assertEquals(response.position(), updated.getPosition())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseRequirement should throw if text in request are same")
    void updateCourseRequirement_shouldThrow_ifTextInRequestAreSame() {
        Long courseId = 1L;

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "Test text 1"
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourseRequirement(
                        requirement.getId(),
                        courseId,
                        request
                )
        );

        CourseRequirement after = repository.findById(requirement.getId()).orElseThrow();

        Assertions.assertAll(
                () -> Assertions.assertEquals(ErrorCode.REQUIREMENT_TEXT_INVALID, ex.getCode()),
                () -> Assertions.assertEquals("Test text 1", after.getText())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updateCourseRequirement should throw if text in request is blank")
    void updateCourseRequirement_shouldThrow_ifTextInRequestIsBlank() {
        Long courseId = 1L;

        CourseRequirement requirement = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );

        UpdateRequirementRequest request = new UpdateRequirementRequest(
                "   "
        );

        AppException ex = Assertions.assertThrows(
                AppException.class,
                () -> service.updateCourseRequirement(
                        requirement.getId(),
                        courseId,
                        request
                )
        );

        CourseRequirement after = repository.findById(requirement.getId()).orElseThrow();

        Assertions.assertAll(
                () -> Assertions.assertEquals(ErrorCode.REQUIREMENT_TEXT_INVALID, ex.getCode()),
                () -> Assertions.assertEquals("Test text 1", after.getText())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("deleteCourseRequirement should shift position after deletion")
    void deleteCourseRequirement_shouldShiftPositionsAfterDeletion() {
        Long courseId = 1L;

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );

        service.deleteCourseRequirement(2L, 1L);

        List<CourseRequirement> requirements = repository.findAllByCourseIdOrderByPositionAsc(1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, requirements.size()),
                () -> Assertions.assertEquals(1, requirements.get(0).getPosition()),
                () -> Assertions.assertEquals(2, requirements.get(1).getPosition())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should correct shift position")
    void updatePosition_shouldCorrectShiftPositions() {
        Long courseId = 1L;

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement4 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 4")
                        .position(4)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement5 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 5")
                        .position(5)
                        .courseId(courseId)
                        .build()
        );

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                2
        );

        service.updatePosition(
                5L,
                1L,
                request
        );

        List<CourseRequirement> points = repository.findAllByCourseIdOrderByPositionAsc(1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, points.size()),

                () -> Assertions.assertEquals(1, points.get(0).getPosition()),
                () -> Assertions.assertEquals("Test text 1", points.get(0).getText()),

                () -> Assertions.assertEquals(2, points.get(1).getPosition()),
                () -> Assertions.assertEquals("Test text 5", points.get(1).getText()),

                () -> Assertions.assertEquals(3, points.get(2).getPosition()),
                () -> Assertions.assertEquals("Test text 2", points.get(2).getText()),

                () -> Assertions.assertEquals(4, points.get(3).getPosition()),
                () -> Assertions.assertEquals("Test text 3", points.get(3).getText()),

                () -> Assertions.assertEquals(5, points.get(4).getPosition()),
                () -> Assertions.assertEquals("Test text 4", points.get(4).getText())
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("updatePosition should correct up position")
    void updatePosition_shouldCorrectUpPositions() {
        Long courseId = 1L;

        CourseRequirement requirement1 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 1")
                        .position(1)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement2 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 2")
                        .position(2)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement3 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 3")
                        .position(3)
                        .courseId(courseId)
                        .build()
        );
        CourseRequirement requirement4 = repository.save(
                CourseRequirementTestFactory.createDefaultCourseRequirement()
                        .text("Test text 4")
                        .position(4)
                        .courseId(courseId)
                        .build()
        );

        ReorderRequirementRequest request = new ReorderRequirementRequest(
                4
        );

        service.updatePosition(
                2L,
                1L,
                request
        );

        List<CourseRequirement> points = repository.findAllByCourseIdOrderByPositionAsc(1L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(4, points.size()),

                () -> Assertions.assertEquals(1, points.get(0).getPosition()),
                () -> Assertions.assertEquals("Test text 1", points.get(0).getText()),

                () -> Assertions.assertEquals(2, points.get(1).getPosition()),
                () -> Assertions.assertEquals("Test text 3", points.get(1).getText()),

                () -> Assertions.assertEquals(3, points.get(2).getPosition()),
                () -> Assertions.assertEquals("Test text 4", points.get(2).getText()),

                () -> Assertions.assertEquals(4, points.get(3).getPosition()),
                () -> Assertions.assertEquals("Test text 2", points.get(3).getText())
        );
    }

}
