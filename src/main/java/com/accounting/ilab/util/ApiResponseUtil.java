package com.accounting.ilab.util;

import com.accounting.ilab.model.response.base.ApiResponse;
import lombok.experimental.UtilityClass;

import java.time.ZonedDateTime;
import java.util.Objects;

@UtilityClass
public class ApiResponseUtil {

    private final static int MAX_EXCEPTION_LENGTH = 4096;

    public static <T> ApiResponse<T> generateGenericErrorResponse(T content) {
        ApiResponse<T> genericResponse = generateGenericResponse(content);

        genericResponse.setSuccess(Boolean.FALSE);

        return genericResponse;
    }

    public static <T> ApiResponse<T> generateGenericResponse(T content) {
        return ApiResponse.<T>builder()
                .content(content)
                .zonedDateTime(ZonedDateTime.now())
                .build();
    }

    public static String getFormattedMessage(String message) {
        message = Objects.nonNull(message) ? message.trim() : "";
        return message.length() > MAX_EXCEPTION_LENGTH ? message.substring(4096) : message;
    }

}
