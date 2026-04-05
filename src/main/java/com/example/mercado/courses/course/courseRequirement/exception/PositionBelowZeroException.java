package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

public class PositionBelowZeroException extends RuntimeException {

    @Getter
    private final String code = "POSITION_BELOW_ZERO";

    public PositionBelowZeroException() {
        super("Position below zero");
    }
}
