package com.example.mercado.courses.course.courseRequirement.controller.publicController;

import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.service.interfaces.CoursePublicRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/requirements")
@RequiredArgsConstructor
public class CoursePublicRequirementController {

    private final CoursePublicRequirementService service;


    @GetMapping("/{requirementId}")
    public RequirementResponse getCourseRequirement(
            @PathVariable Long requirementId,
            @PathVariable Long courseId
    ) {
        return service.getCourseRequirement(requirementId, courseId);
    }



    @GetMapping
    public List<RequirementResponse> getAllByCourseId(
            @PathVariable Long courseId
    ) {
        return service.getAllByCourseId(courseId);
    }


}
