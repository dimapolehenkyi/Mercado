package com.example.mercado.courses.course.courseLearningPoint.service.publicService;

import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import com.example.mercado.courses.course.courseLearningPoint.mapper.CourseLearningPointMapper;
import com.example.mercado.courses.course.courseLearningPoint.repository.CourseLearningPointRepository;
import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CoursePublicLearningPointService;
import com.example.mercado.courses.course.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePublicLearningPointServiceImpl implements CoursePublicLearningPointService {


    private final CourseLearningPointMapper mapper;
    private final CourseLearningPointRepository repository;

    private final EntityFinder finder;



    @Override
    @Transactional(readOnly = true)
    public LearningPointResponse getCourseLearningPoint(Long pointId, Long courseId) {
        CourseLearningPoint point = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(pointId, courseId),
                ErrorCode.LEARNING_POINT_NOT_FOUND,
                pointId,
                courseId
        );

        return mapper.toResponse(point);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningPointResponse> getCourseLearningPoints(Long courseId) {
        List<CourseLearningPoint> points = repository.findAllByCourseIdOrderByPositionAsc(courseId);

        return points.stream()
                .map(mapper::toResponse)
                .toList();
    }

}
