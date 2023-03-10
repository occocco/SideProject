package com.toy.overall_practice.exception.exhandler.advice;

import com.toy.overall_practice.exception.DuplicateMemberException;
import com.toy.overall_practice.exception.ForbiddenException;
import com.toy.overall_practice.exception.exhandler.ExResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(ForbiddenException.class)
    public ExResult forbiddenException(ForbiddenException e) {
        log.error("[ForbiddenException]", e);
        return new ExResult("403", e.getMessage());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ExResult duplicateMemberException(DuplicateMemberException e) {
        return new ExResult("403", e.getMessage());
    }
}
