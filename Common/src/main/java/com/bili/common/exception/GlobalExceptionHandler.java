package com.bili.common.exception;

import com.bili.common.utils.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理异常
    @ExceptionHandler(Exception.class)
    public Result ex(Exception e){
        //处理注解验证异常
        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation GET object param
            BindException ex = (MethodArgumentNotValidException) e;
            return Result.validateFailed(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
        }
        else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            ConstraintViolationException ex = (ConstraintViolationException) e;
            return Result.validateFailed(Objects.requireNonNull(ex.getConstraintViolations().iterator().next().getMessage()));
        }
        else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            return Result.validateFailed(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
        }
        else if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException ex = (HttpMessageNotReadableException) e;
            return Result.validateFailed("参数错误");
        }

        StringBuilder message = new StringBuilder(e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            message.append("\n").append(stackTraceElement);
        }
        log.error(String.valueOf(message));
        return Result.failed("服务异常，请稍后重试");
    }
}
