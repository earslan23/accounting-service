package com.accounting.ilab.mapper;


import com.accounting.ilab.entity.Invoice;
import com.accounting.ilab.model.request.InvoiceRequestDto;
import com.accounting.ilab.model.response.InvoiceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InvoiceMapper {

    List<InvoiceResponseDto> fromEntityListToDtoList (final List<Invoice> invoices);

    @Mapping(target = "productId", source = "invoice.product.id")
    @Mapping(target = "productName", source = "invoice.product.name")
    InvoiceResponseDto fromEntityToDto(final Invoice invoice);

    Invoice fromDtoToEntity(final InvoiceRequestDto invoiceRequestDto);


}
