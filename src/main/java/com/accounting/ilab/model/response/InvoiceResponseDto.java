package com.accounting.ilab.model.response;


import com.accounting.ilab.model.enums.InvoiceStatus;

public record InvoiceResponseDto(String firstName,
                                 String lastName,
                                 String email,
                                 Long productId,
                                 String productName,
                                 Long billNo,
                                 InvoiceStatus status
) {
}
