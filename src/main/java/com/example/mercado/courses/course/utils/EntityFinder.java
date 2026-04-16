package com.example.mercado.courses.course.utils;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class EntityFinder {

    public <E> E findEntityOrThrow(
            Supplier<Optional<E>> supplier,
            ErrorCode errorCode,
            Object... args
    ) {
        return supplier.get()
                .orElseThrow(() -> new AppException(errorCode, args));
    }

}
