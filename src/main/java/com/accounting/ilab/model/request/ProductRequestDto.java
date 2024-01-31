package com.accounting.ilab.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDto (
        String description,
        @NotNull BigDecimal price,
        @NotBlank String name
){}
