package com.example.mercado.courses.course.customValidators.price.validPriceRange;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = PriceRangeValidator.class
)
public @interface ValidPriceRange {

    String message() default "priceFrom must be less than or equal to priceTo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
