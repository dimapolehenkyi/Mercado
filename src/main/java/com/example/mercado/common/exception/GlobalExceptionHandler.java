package com.example.mercado.common.exception;


import com.example.mercado.courses.assignment.exception.AssignmentAlreadyExistException;
import com.example.mercado.courses.assignment.exception.AssignmentNotFoundException;
import com.example.mercado.courses.course.exception.CourseAlreadyArchivedException;
import com.example.mercado.courses.course.exception.CourseAlreadyExistsException;
import com.example.mercado.courses.course.exception.CourseAlreadyPublishedException;
import com.example.mercado.courses.course.exception.CourseNotFound;
import com.example.mercado.courses.lesson.exception.LessonAlreadyExistException;
import com.example.mercado.courses.lesson.exception.LessonNotFoundException;
import com.example.mercado.courses.lesson.exception.LessonStatusAreSameException;
import com.example.mercado.courses.lessonContent.exception.LessonContentAlreadyExistsException;
import com.example.mercado.courses.lessonContent.exception.LessonContentNotFoundException;
import com.example.mercado.courses.module.exception.*;
import com.example.mercado.courses.moduleResource.exception.ModuleResourceAlreadyExistsException;
import com.example.mercado.courses.moduleResource.exception.ModuleResourceNotFound;
import com.example.mercado.mail.exceptions.mailException.EmailSendFailedException;
import com.example.mercado.users.exception.userException.InvalidUserRoleException;
import com.example.mercado.users.exception.userException.UserAlreadyExistsException;
import com.example.mercado.users.exception.userException.UserInactiveException;
import com.example.mercado.users.exception.userException.UserNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFount(@NonNull UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(@NonNull UserAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<ErrorResponse> handleUserInactive(@NonNull UserInactiveException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserRole(@NonNull InvalidUserRoleException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<ErrorResponse> emailSendFailed(@NonNull EmailSendFailedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCourseAlreadyExists(@NonNull CourseAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(CourseNotFound.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFound(@NonNull CourseNotFound ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(CourseAlreadyPublishedException.class)
    public ResponseEntity<ErrorResponse> handleCourseAlreadyPublished(@NonNull CourseAlreadyPublishedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(CourseAlreadyArchivedException.class)
    public ResponseEntity<ErrorResponse> handleCourseAlreadyArchived(@NonNull CourseAlreadyArchivedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleModuleAlreadyExists(@NonNull ModuleAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleModuleNotFound(@NonNull ModuleNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(ModuleAlreadyArchivedException.class)
    public ResponseEntity<ErrorResponse> handleModuleAlreadyArchived(@NonNull ModuleAlreadyArchivedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleAlreadyPublishedException.class)
    public ResponseEntity<ErrorResponse> handleModuleAlreadyPublished(@NonNull ModuleAlreadyPublishedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleModuleAlreadyDeleted(@NonNull ModuleAlreadyDeletedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleAlreadyHaveFirstPosition.class)
    public ResponseEntity<ErrorResponse> handleModuleAlreadyHaveFirstPosition(@NonNull ModuleAlreadyHaveFirstPosition ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleAlreadyHaveLastPositionException.class)
    public ResponseEntity<ErrorResponse> handleModuleAlreadyHaveLastPosition(@NonNull ModuleAlreadyHaveLastPositionException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleModuleResourceAlreadyExists(@NonNull ModuleResourceAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ModuleResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleModuleResourceNotFound(@NonNull ModuleResourceNotFound ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return   ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(LessonContentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLessonContentAlreadyExists(@NonNull LessonContentAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return   ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(LessonContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLessonContentNotFound(@NonNull LessonContentNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLessonNotFound(@NonNull LessonNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(LessonStatusAreSameException.class)
    public ResponseEntity<ErrorResponse> handleLessonStatusAreSame(@NonNull LessonStatusAreSameException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(LessonAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleLessonAlreadyExist(@NonNull LessonAlreadyExistException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(AssignmentAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleAssignmentAlreadyExist(@NonNull AssignmentAlreadyExistException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAssignmentNotFound(@NonNull AssignmentNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(@NonNull BadCredentialsException ex) {
        return ex.getMessage();
    }

}


