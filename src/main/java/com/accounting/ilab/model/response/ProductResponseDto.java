package com.accounting.ilab.model.response;


import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String description,
        BigDecimal price,
        String name) {
}
