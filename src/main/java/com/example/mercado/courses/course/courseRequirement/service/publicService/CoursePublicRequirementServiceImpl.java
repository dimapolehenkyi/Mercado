package com.example.mercado.courses.course.courseRequirement.service.publicService;

import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapper;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePublicRequirementServiceImpl implements CoursePublicRequirementService {


    private final CourseRequirementRepository repository;
    private final CourseRequirementMapper mapper;

    private final EntityFinder finder;



    @Override
    @Transactional(readOnly = true)
    public RequirementResponse getCourseRequirement(
            Long requirementId,
            Long courseId
    ) {
        CourseRequirement requirement = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(requirementId, courseId),
                ErrorCode.REQUIREMENT_NOT_FOUND,
                requirementId,
                courseId
        );

        return mapper.toResponse(requirement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequirementResponse> getAllByCourseId(
            Long courseId
    ) {
        List<CourseRequirement> requirements = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        return requirements.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
