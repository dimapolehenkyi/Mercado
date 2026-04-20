package com.example.mercado.courses.course.courseLearningPoint.service;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapper;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import com.example.mercado.courses.course.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class CourseAdminLearningPointServiceImpl implements CourseAdminLearningPointService {


    private final CourseLearningPointMapper mapper;
    private final CourseLearningPointRepository repository;

    private final EntityFinder finder;


    @Override
    @Transactional
    public LearningPointResponse createCourseLearningPoint(
            Long courseId,
            AddLearningPointRequest request
    ) {
        if (repository.existsByCourseIdAndText(courseId, request.text())) {
            throw new AppException(
                    ErrorCode.LEARNING_POINT_ALREADY_EXISTS,
                    request.text()
            );
        }

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);
        if (maxPos != null && maxPos >= 10) {
            throw new AppException(
                    ErrorCode.LEARNING_POINT_LIMIT_REACHED
            );
        }
        int nextPos = (maxPos == null) ? 0 : maxPos + 1;

        CourseLearningPoint point = mapper.toEntity(courseId, request);
        point.setPosition(nextPos);

        repository.save(point);

        return mapper.toResponse(point);
    }

    @Override
    @Transactional
    public LearningPointResponse updateCourseLearningPoint(
            Long pointId,
            Long courseId,
            UpdateLearningPointRequest request
    ) {
        CourseLearningPoint point = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(pointId, courseId),
                ErrorCode.LEARNING_POINT_NOT_FOUND,
                pointId,
                courseId
        );

        point.setText(request.text());

        return mapper.toResponse(point);
    }

    @Override
    @Transactional
    public void deleteCourseLearningPoint(
            Long pointId,
            Long courseId
    ) {
        CourseLearningPoint point = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(pointId, courseId),
                ErrorCode.LEARNING_POINT_NOT_FOUND,
                pointId,
                courseId
        );

        int deletedPos = point.getPosition();
        Integer maxPos = repository.findMaxPositionByCourseId(courseId);

        repository.delete(point);

        repository.decrementPositionRange(
                courseId,
                deletedPos + 1,
                maxPos
        );
    }

    @Override
    @Transactional
    public LearningPointResponse updatePositionCourseLearningPoint(
            Long pointId,
            Long courseId,
            ReorderLearningPointRequest request
    ) {
        CourseLearningPoint point = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(pointId, courseId),
                ErrorCode.LEARNING_POINT_NOT_FOUND,
                pointId,
                courseId
        );

        int oldPos = point.getPosition();
        int newPos = request.position();

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);

        if (newPos < 0 || newPos > maxPos) {
            throw new AppException(ErrorCode.LEARNING_POINT_POSITION_INVALID);
        }

        if (oldPos == newPos) {
            return mapper.toResponse(point);
        }

        if (newPos < oldPos) {
            repository.incrementPositionRange(courseId, newPos, oldPos - 1);
        } else {
            repository.decrementPositionRange(courseId, oldPos + 1, newPos);
        }

        point.setPosition(newPos);

        return mapper.toResponse(point);
    }
}
