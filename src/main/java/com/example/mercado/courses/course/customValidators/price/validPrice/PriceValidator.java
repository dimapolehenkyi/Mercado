package com.example.mercado.courses.course.customValidators.price.validPrice;

import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.enums.CourseAccessType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PriceValidator implements ConstraintValidator<ValidPrice, CreateCourseRequest> {

    @Override
    public boolean isValid(CreateCourseRequest request, ConstraintValidatorContext context) {
        if (request.type() == CourseAccessType.FREE) {
            if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) != 0) {
                return false;
            }
        }

        if (request.type() == CourseAccessType.PAID) {
            if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
        }

        return true;
    }

}
