package com.accounting.ilab.mapper;


import com.accounting.ilab.entity.Product;
import com.accounting.ilab.model.request.ProductRequestDto;
import com.accounting.ilab.model.response.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductMapper {

    List<ProductResponseDto> fromEntityListToDtoList(final List<Product> products);

    ProductResponseDto fromEntityToDto(final Product product);

    Product fromDtoToEntity(final ProductRequestDto productRequestDto);

    void updateFromDto(ProductRequestDto productRequestDto, @MappingTarget Product product);

}
