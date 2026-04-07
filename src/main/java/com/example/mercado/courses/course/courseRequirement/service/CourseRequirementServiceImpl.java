package com.example.mercado.courses.course.courseRequirement.service;

import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.exception.CourseRequirementNotFoundException;
import com.example.mercado.courses.course.courseRequirement.exception.CourseRequirementExistByCourseIdAndTextException;
import com.example.mercado.courses.course.courseRequirement.exception.TooManyRequirementException;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapper;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.courseRequirement.service.interfaces.CourseRequirementService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseRequirementServiceImpl implements CourseRequirementService {

    private final CourseRequirementRepository repository;

    private final CourseRequirementMapper mapper;



    @Override
    public RequirementResponse addCourseRequirement(Long courseId, AddRequirementRequest request) {
        if (repository.existsByCourseIdAndText(courseId, request.text())) throw new CourseRequirementExistByCourseIdAndTextException(courseId);

        CourseRequirement requirement = mapper.toEntity(courseId, request);

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);
        int nextPos = (maxPos == null) ? 0 : maxPos + 1;
        if (nextPos >= 10) {
            throw new TooManyRequirementException(courseId);
        }
        requirement.setPosition(nextPos);

        CourseRequirement saved = repository.save(requirement);

        return mapper.toResponse(saved);
    }

    @Override
    public RequirementResponse updateCourseRequirement(Long requirementId, Long courseId, UpdateRequirementRequest request) {
        CourseRequirement requirement = getCourseRequirementOrThrow(requirementId, courseId);

        requirement.setText(request.text());

        return mapper.toResponse(requirement);
    }

    @Override
    public RequirementResponse getCourseRequirement(Long requirementId, Long courseId) {
        CourseRequirement requirement = getCourseRequirementOrThrow(requirementId, courseId);

        return mapper.toResponse(requirement);
    }

    @Override
    public void deleteCourseRequirement(Long requirementId, Long courseId) {
        CourseRequirement requirement = getCourseRequirementOrThrow(requirementId, courseId);

        repository.decrementPositionRange(
                courseId,
                requirement.getPosition() + 1,
                repository.countByCourseId(courseId)
        );

        repository.delete(requirement);
    }

    @Override
    public List<RequirementResponse> getAllByCourseId(Long courseId) {
        List<CourseRequirement> requirements = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        return requirements.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RequirementResponse updatePosition(Long requirementId, Long courseId, ReorderRequirementRequest request) {
        CourseRequirement current = getCourseRequirementOrThrow(requirementId, courseId);

        repository.lockByCourseId(courseId);

        int oldPos = current.getPosition();
        int newPos = request.position();

        if (oldPos == newPos) {
            return mapper.toResponse(current);
        }

        if (newPos < oldPos) {
            repository.incrementPositionRange(courseId, newPos, oldPos - 1);
        } else {
            repository.decrementPositionRange(courseId, oldPos + 1, newPos);
        }

        current.setPosition(newPos);

        return mapper.toResponse(current);
    }


    private CourseRequirement getCourseRequirementOrThrow(
            @NonNull Long courseRequirementId,
            @NonNull Long courseId
    ) {
        return  repository.findByIdAndCourseId(courseRequirementId, courseId)
                .orElseThrow(() -> new CourseRequirementNotFoundException(courseId, courseRequirementId));
    }
}
