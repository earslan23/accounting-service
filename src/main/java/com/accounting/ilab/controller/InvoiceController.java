package com.accounting.ilab.controller;


import com.accounting.ilab.model.enums.InvoiceStatus;
import com.accounting.ilab.model.request.InvoiceRequestDto;
import com.accounting.ilab.model.response.InvoiceResponseDto;
import com.accounting.ilab.model.response.base.ApiResponse;
import com.accounting.ilab.model.response.base.TPage;
import com.accounting.ilab.service.InvoiceService;
import com.accounting.ilab.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("v1")
    public ApiResponse<InvoiceResponseDto> addInvoice(@Valid @RequestBody InvoiceRequestDto invoiceRequestDto) {
        final var invoice = invoiceService.addInvoice(invoiceRequestDto);
        return ApiResponseUtil.generateGenericResponse(invoice);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("v1")
    public ApiResponse<TPage<InvoiceResponseDto>> getInvoicesByStatusPageable(@RequestParam(defaultValue = "APPROVED") InvoiceStatus status,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {

        final var invoice = invoiceService.getInvoicesByStatusPageable(status, PageRequest.of(page, size));
        return ApiResponseUtil.generateGenericResponse(invoice);
    }

}
