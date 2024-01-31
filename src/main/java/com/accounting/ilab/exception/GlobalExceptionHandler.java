package com.accounting.ilab.exception;

import com.accounting.ilab.model.response.base.ApiResponse;
import com.accounting.ilab.model.response.base.ErrorResponse;
import com.accounting.ilab.util.ApiResponseUtil;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleCustomException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(ApiResponseUtil.getFormattedMessage(ex.getStackTrace().toString()))
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        log.error("Exception occurred : {}" , ex);
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleGeneralException(BadRequestException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .build();

        log.warn("BadRequestException occurred : {}" , ex);
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }


    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleGeneralException(BusinessException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .build();

        log.warn("BusinessException occurred : {}" , ex);
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleConstraintViolation(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.addFieldError(com.accounting.ilab.model.response.base.FieldError.builder().field(fieldName).errorMessage(errorMessage).build());
        });
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleValidationExceptions(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        ex.getConstraintViolations().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getMessage();
            errorResponse.addFieldError(com.accounting.ilab.model.response.base.FieldError.builder().field(fieldName).errorMessage(errorMessage).build());
        });
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }

    @ExceptionHandler(Throwable.class)
    public ApiResponse<ErrorResponse> handleThrowable(final Throwable exception) {
        log.error("handleThrowable occurred : {} ", exception);
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(ApiResponseUtil.getFormattedMessage(exception.getMessage()));
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ApiResponse<ErrorResponse>  handleAccessDeniedException(final AccessDeniedException ex)  {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .build();

        log.warn("handleAccessDeniedException occurred : {}" , ex);
        return ApiResponseUtil.generateGenericErrorResponse(errorResponse);
    }

}