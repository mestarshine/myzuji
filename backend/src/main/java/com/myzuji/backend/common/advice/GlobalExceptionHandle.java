package com.myzuji.backend.common.advice;

import com.myzuji.backend.common.util.ResultWrapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(Exception.class)
    public ResultWrapper customException() {
        return ResultWrapper.builder().code("301").msg("统一异常").build();
    }

    @ExceptionHandler(Exception.class)
    public ResultWrapper customException(Exception e) {
        return ResultWrapper.builder().code("301").msg(e.getMessage()).build();
    }
}
