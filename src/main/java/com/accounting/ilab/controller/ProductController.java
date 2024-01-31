package com.accounting.ilab.controller;


import com.accounting.ilab.model.request.ProductRequestDto;
import com.accounting.ilab.model.response.ProductResponseDto;
import com.accounting.ilab.model.response.base.ApiResponse;
import com.accounting.ilab.service.ProductService;
import com.accounting.ilab.util.ApiResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("v1/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable @NotNull @Min(value = 1) Long productId) {
        final var product = productService.getProductById(productId);
        return ApiResponseUtil.generateGenericResponse(product);
    }

    @GetMapping("v1")
    public ApiResponse<List<ProductResponseDto>> getProductList() {
        final var productList = productService.getProductList();
        return ApiResponseUtil.generateGenericResponse(productList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("v1/{productId}")
    public ApiResponse<Boolean> deleteProductById(@PathVariable @NotNull @Min(value = 1) Long productId) {
        productService.deleteProductById(productId);
        return ApiResponseUtil.generateGenericResponse(Boolean.TRUE);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("v1")
    public ApiResponse<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        final var product = productService.addProduct(productRequestDto);
        return ApiResponseUtil.generateGenericResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("v1/{productId}")
    public ApiResponse<ProductResponseDto> updateProduct(@PathVariable Long productId , @Valid @RequestBody ProductRequestDto productRequestDto) {
        final var product = productService.updateProduct(productId, productRequestDto);
        return ApiResponseUtil.generateGenericResponse(product);
    }


}
