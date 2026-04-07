package com.example.mercado.courses.course.courseLearningPoint.service;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.ReorderLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.exception.CourseLearningPointAlreadyExistException;
import com.example.mercado.courses.course.courseLearningPoint.exception.CourseLearningPointHasTooManyPointsException;
import com.example.mercado.courses.course.courseLearningPoint.exception.CourseLearningPointNotFoundException;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapper;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseLearningPointService;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import com.example.mercado.courses.course.courseRequirement.exception.CourseRequirementNotFoundException;
import com.example.mercado.courses.course.courseRequirement.exception.TooManyRequirementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseLearningPointServiceImpl implements CourseLearningPointService {


    private final CourseLearningPointMapper mapper;

    private final CourseLearningPointRepository repository;


    @Override
    public LearningPointResponse createCourseLearningPoint(Long courseId, AddLearningPointRequest request) {
        if (repository.existsByCourseIdAndText(courseId, request.text())) {
            throw new CourseLearningPointAlreadyExistException(courseId);
        }

        repository.lockByCourseId(courseId);

        CourseLearningPoint point = mapper.toEntity(courseId, request);

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);
        int nextPos = (maxPos == null) ? 0 : maxPos + 1;
        if (nextPos >= 10) {
            throw new CourseLearningPointHasTooManyPointsException(courseId);
        }
        point.setPosition(nextPos);

        repository.save(point);

        return mapper.toResponse(point);
    }

    @Override
    public LearningPointResponse updateCourseLearningPoint(Long pointId, Long courseId, UpdateLearningPointRequest request) {
        CourseLearningPoint point = getCourseLearningPointOrThrow(pointId, courseId);

        point.setText(request.text());

        return mapper.toResponse(point);
    }

    @Override
    public LearningPointResponse getCourseLearningPoint(Long pointId, Long courseId) {
        CourseLearningPoint point = getCourseLearningPointOrThrow(pointId, courseId);

        return mapper.toResponse(point);
    }

    @Override
    public void deleteCourseLearningPoint(Long pointId, Long courseId) {
        CourseLearningPoint point = getCourseLearningPointOrThrow(pointId, courseId);

        repository.decrementPositionRange(
                courseId,
                point.getPosition() + 1,
                repository.countByCourseId(courseId)
        );

        repository.delete(point);
    }

    @Override
    public List<LearningPointResponse> getAllLearningPointsByCourseId(Long courseId) {
        List<CourseLearningPoint> points = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        return points.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public LearningPointResponse updatePositionCourseLearningPoint(Long pointId, Long courseId, ReorderLearningPointRequest request) {
        CourseLearningPoint point = getCourseLearningPointOrThrow(pointId, courseId);

        repository.lockByCourseId(courseId);

        int oldPos = point.getPosition();
        int newPos = request.position();

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




    private CourseLearningPoint getCourseLearningPointOrThrow(
            @NonNull Long pointId,
            @NonNull Long courseId
    ) {
        return  repository.findByIdAndCourseId(pointId, courseId)
                .orElseThrow(() -> new CourseLearningPointNotFoundException(courseId, pointId));
    }
}
