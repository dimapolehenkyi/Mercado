package com.example.mercado.courses.course.courseLearningPoint.mapper;

import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import org.springframework.stereotype.Component;

@Component
public class CourseLearningPointMapper {

    public CourseLearningPoint toEntity(Long courseId, AddLearningPointRequest request) {
        return CourseLearningPoint.builder()
                .courseId(courseId)
                .text(request.text())
                .build();
    }

    public void updateEntity(CourseLearningPoint point, UpdateLearningPointRequest request) {
        point.setText(request.text());
    }

    public LearningPointResponse toResponse(CourseLearningPoint point) {
        return new LearningPointResponse(
                point.getId(),
                point.getCourseId(),
                point.getText(),
                point.getPosition()
        );
    }

}
