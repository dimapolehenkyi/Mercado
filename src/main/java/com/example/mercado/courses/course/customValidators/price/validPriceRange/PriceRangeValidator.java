package com.example.mercado.courses.course.customValidators.price.validPriceRange;

import com.example.mercado.courses.course.dto.CourseSearchFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, CourseSearchFilter> {

    @Override
    public boolean isValid(CourseSearchFilter filter, ConstraintValidatorContext context) {
        if (Boolean.TRUE.equals(filter.isFree())) {
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
