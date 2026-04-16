package com.example.mercado.courses.course.customValidators.price.validPriceRange;

import com.example.mercado.courses.course.dto.CourseSearchFilter;
import com.example.mercado.courses.course.enums.CourseAccessType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, CourseSearchFilter> {

    @Override
    public boolean isValid(CourseSearchFilter filter, ConstraintValidatorContext context) {
        if (CourseAccessType.FREE.equals(filter.type())) {
            return filter.priceFrom() == null && filter.priceTo() == null;
        }

        if (filter.priceFrom() == null && filter.priceTo() == null) {
            return true;
        }

        if (filter.priceFrom() == null || filter.priceTo() == null) {
            return false;
        }

        return filter.priceFrom().compareTo(filter.priceTo()) <= 0;
    }
}
