package com.accounting.ilab.model.response.base;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldError {

    private String field;
    private String errorMessage;

}
