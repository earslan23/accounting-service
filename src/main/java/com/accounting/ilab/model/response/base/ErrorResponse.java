package com.accounting.ilab.model.response.base;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private Integer httpStatus;
    private String message;
    private List<FieldError> fieldErrors;

    public void addFieldError(FieldError fieldError) {
        if (CollectionUtils.isEmpty(fieldErrors)) {
            this.fieldErrors = new ArrayList<>();
        }

        this.fieldErrors.add(fieldError);
    }

}
