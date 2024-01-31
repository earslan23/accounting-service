package com.accounting.ilab.service.base;


import com.accounting.ilab.model.response.ProductResponseDto;
import com.accounting.ilab.service.ProductService;
import com.accounting.ilab.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public abstract class ProductBaseCache {

    private final BeanUtil beanUtil;

    protected void remove(Long id) {
        Map<Long, ProductResponseDto> productResponseMap = getProxy().getProductMap();
        productResponseMap.remove(id);
    }

    protected void merge(Long id, ProductResponseDto productResponseDto) {
        Map<Long, ProductResponseDto> productResponseMap = getProxy().getProductMap();
        productResponseMap.put(id, productResponseDto);
    }

    public ProductService getProxy() {
        return beanUtil.getBeanOfType(ProductService.class);
    }

}
