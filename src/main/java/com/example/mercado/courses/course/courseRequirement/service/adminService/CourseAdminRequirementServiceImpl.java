package com.example.mercado.courses.course.courseRequirement.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.ReorderRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.mapper.CourseRequirementMapper;
import com.example.mercado.courses.course.courseRequirement.repository.CourseRequirementRepository;
import com.example.mercado.courses.course.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class CourseAdminRequirementServiceImpl implements CourseAdminRequirementService {


    private final CourseRequirementRepository repository;
    private final CourseRequirementMapper mapper;

    private final EntityFinder finder;


    @Override
    @Transactional
    public RequirementResponse createCourseRequirement(
            Long courseId,
            AddRequirementRequest request
    ) {
        if (repository.existsByCourseIdAndText(courseId, request.text())) {
            throw new AppException(
                    ErrorCode.REQUIREMENT_ALREADY_EXISTS,
                    request.text()
            );
        }

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);
        if (maxPos != null && maxPos >= 10) {
            throw new AppException(
                    ErrorCode.REQUIREMENT_LIMIT_REACHED
            );
        }
        int nextPos = (maxPos == null) ? 0 : maxPos + 1;

        CourseRequirement requirement = mapper.toEntity(courseId, request);
        requirement.setPosition(nextPos);

        repository.save(requirement);

        return mapper.toResponse(requirement);
    }

    @Override
    @Transactional
    public RequirementResponse updateCourseRequirement(
            Long requirementId,
            Long courseId,
            UpdateRequirementRequest request
    ) {
        CourseRequirement requirement = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(requirementId, courseId),
                ErrorCode.REQUIREMENT_NOT_FOUND,
                requirementId,
                courseId
        );

        requirement.setText(request.text());

        return mapper.toResponse(requirement);
    }

    @Override
    @Transactional
    public void deleteCourseRequirement(
            Long requirementId,
            Long courseId
    ) {
        CourseRequirement requirement = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(requirementId, courseId),
                ErrorCode.REQUIREMENT_NOT_FOUND,
                requirementId,
                courseId
        );

        int deletedPos = requirement.getPosition();

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);

        repository.delete(requirement);

        if (deletedPos < maxPos) {
            repository.decrementPositionRange(
                    courseId,
                    deletedPos + 1,
                    maxPos
            );
        }
    }

    @Override
    @Transactional
    public RequirementResponse updatePosition(
            Long requirementId,
            Long courseId,
            ReorderRequirementRequest request
    ) {
        CourseRequirement current = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(requirementId, courseId),
                ErrorCode.REQUIREMENT_NOT_FOUND,
                requirementId,
                courseId
        );

        int oldPos = current.getPosition();
        int newPos = request.position();

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);

        if (newPos < 0 || newPos > maxPos) {
            throw new AppException(
                    ErrorCode.REQUIREMENT_POSITION_INVALID
            );
        }

        if (oldPos == newPos) {
            return mapper.toResponse(current);
        }

        repository.updatePosition(requirementId, -1000);

        if (newPos < oldPos) {
            repository.incrementPositionRange(courseId, newPos, oldPos - 1);
        } else {
            repository.decrementPositionRange(courseId, oldPos + 1, newPos);
        }

        repository.updatePosition(requirementId, newPos);

        return mapper.toResponse(current);
    }
}
