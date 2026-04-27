package com.example.mercado.courses.course.courseRequirement.controller.publicController;

import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.service.publicService.CoursePublicRequirementService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/requirements")
@Validated
@RequiredArgsConstructor
public class CoursePublicRequirementController {

    private final CoursePublicRequirementService service;


    @GetMapping("/{requirementId}")
    public RequirementResponse getCourseRequirement(
            @PathVariable @Positive Long requirementId,
            @PathVariable @Positive Long courseId
    ) {
        return service.getCourseRequirement(requirementId, courseId);
    }



    @GetMapping
    public List<RequirementResponse> getAllByCourseId(
            @PathVariable @Positive Long courseId
    ) {
        return service.getAllByCourseId(courseId);
    }


}
