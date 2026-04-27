package com.example.mercado.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //COURSE
    COURSE_NOT_FOUND("course.not.found", HttpStatus.NOT_FOUND),
    COURSE_ALREADY_EXISTS("course.already.exists", HttpStatus.CONFLICT),
    COURSE_STATUS_INVALID("course.status.invalid", HttpStatus.CONFLICT),
    COURSE_ACCESS_TYPE_INVALID("course.access.type.invalid", HttpStatus.CONFLICT),
    COURSE_PRICE_INVALID("course.price.invalid", HttpStatus.CONFLICT),
    COURSE_NAME_INVALID("course.name.invalid", HttpStatus.CONFLICT),
    COURSE_LEVEL_INVALID("course.level.invalid", HttpStatus.CONFLICT),

    //COURSE REQUIREMENT
    REQUIREMENT_ALREADY_EXISTS("requirement.already.exists", HttpStatus.CONFLICT),
    REQUIREMENT_NOT_FOUND("requirement.not.found", HttpStatus.NOT_FOUND),
    REQUIREMENT_POSITION_INVALID("requirement.position.invalid", HttpStatus.BAD_REQUEST),
    REQUIREMENT_TEXT_INVALID("requirement.text.invalid", HttpStatus.BAD_REQUEST),
    REQUIREMENT_LIMIT_REACHED("requirement.limit.reached", HttpStatus.BAD_REQUEST),

    //COURSE LEARNING POINT
    LEARNING_POINT_ALREADY_EXISTS("learning.points.already.exists", HttpStatus.CONFLICT),
    LEARNING_POINT_SAME_TEXT("learning.points.same.text", HttpStatus.BAD_REQUEST),
    LEARNING_POINT_LIMIT_REACHED("learning.points.limit.reached", HttpStatus.BAD_REQUEST),
    LEARNING_POINT_NOT_FOUND("learning.points.not.found", HttpStatus.NOT_FOUND),
    LEARNING_POINT_POSITION_INVALID("learning.points.position.invalid", HttpStatus.BAD_REQUEST),

    //LESSON
    LESSON_ALREADY_EXISTS("lesson.already.exists", HttpStatus.CONFLICT),
    LESSON_NOT_FOUND("lesson.not.found", HttpStatus.NOT_FOUND),
    LESSON_STATUS_INVALID("lesson.status.invalid", HttpStatus.BAD_REQUEST),

    //LESSON CONTENT
    LESSON_CONTENT_ALREADY_EXISTS("lesson.content.already.exists", HttpStatus.CONFLICT),
    LESSON_CONTENT_NOT_FOUND("lesson.content.not.found", HttpStatus.NOT_FOUND),

    //MODULE
    MODULE_ALREADY_ARCHIVED("module.already.archived", HttpStatus.CONFLICT),
    MODULE_ALREADY_PUBLISHED("module.already.published", HttpStatus.BAD_REQUEST),
    MODULE_NOT_FOUND("module.not.found", HttpStatus.NOT_FOUND),
    MODULE_POSITION_INVALID("module.position.invalid", HttpStatus.BAD_REQUEST),
    MODULE_ALREADY_DELETED("module.already.deleted", HttpStatus.CONFLICT),
    MODULE_ALREADY_EXISTS("module.already.exists", HttpStatus.CONFLICT),

    //MODULE CONTENT
    MODULE_CONTENT_ALREADY_EXISTS("module.content.already.exists", HttpStatus.CONFLICT),
    MODULE_CONTENT_NOT_FOUND("module.content.not.found", HttpStatus.NOT_FOUND),

    //ASSIGNMENT
    ASSIGNMENT_ALREADY_EXISTS("assignment.already.exists", HttpStatus.CONFLICT),
    ASSIGNMENT_NOT_FOUND("assignment.not.found", HttpStatus.NOT_FOUND),

    //USER
    INVALID_USER_ROLE("invalid.user.role", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS("user.already.exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND("user.not.found", HttpStatus.NOT_FOUND),
    USER_INACTIVATE("user.inactivate", HttpStatus.FORBIDDEN),

    //MAIL
    MAIL_SEND_FAILED("mail.send.failed", HttpStatus.CONFLICT),

    //VALIDATION
    VALIDATION_ERROR("validation.error", HttpStatus.BAD_REQUEST);

    private final String key;
    private final HttpStatus status;

    ErrorCode(String key, HttpStatus status) {
        this.key = key;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }
}
