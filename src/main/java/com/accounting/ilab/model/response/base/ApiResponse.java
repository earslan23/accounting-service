package com.accounting.ilab.model.response.base;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    @Builder.Default
    private boolean success = Boolean.TRUE;

    private T content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime zonedDateTime;

    public ApiResponse(boolean res, String message) {
    }
}
