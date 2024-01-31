package com.accounting.ilab.service;


import com.accounting.ilab.entity.Product;
import com.accounting.ilab.exception.BadRequestException;
import com.accounting.ilab.mapper.ProductMapper;
import com.accounting.ilab.model.request.ProductRequestDto;
import com.accounting.ilab.model.response.ProductResponseDto;
import com.accounting.ilab.repos.ProductRepository;
import com.accounting.ilab.service.base.ProductBaseCache;
import com.accounting.ilab.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService extends ProductBaseCache {

    private final ProductRepository productRepository;
    private final MessageService messageService;
    private final ProductMapper productMapper;

    public ProductService(BeanUtil beanUtil, ProductRepository productRepository,
                          MessageService messageService, ProductMapper productMapper) {
        super(beanUtil);
        this.productRepository = productRepository;
        this.messageService = messageService;
        this.productMapper = productMapper;
    }

    public List<ProductResponseDto> getProductList() {
        return getProxy().getProductMap().values().stream().collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(final Long id) {
        return Optional.ofNullable(getProxy().getProductMap().get(id))
                .orElseThrow(() -> new BadRequestException(messageService.getLocaleMessage("error.message.product.not.found")));
    }


    @Cacheable(value = "map-products-cache", unless = "#result==null")
    public Map<Long, ProductResponseDto> getProductMap() {

        final List<Product> products = productRepository.findAll();
        final List<ProductResponseDto> productResponseDtos = productMapper.fromEntityListToDtoList(products);

        return productResponseDtos.stream().collect(Collectors.toMap(ProductResponseDto::id, Function.identity()));
    }

    @CacheEvict(value = "map-products-cache", key = "#id")
    public void deleteProductById(final Long id) {
        productRepository.deleteById(id);
        remove(id);
        log.warn("Product deleted by this id: {}", id);
    }


    @CachePut(value = "map-products-cache")
    public ProductResponseDto addProduct(final ProductRequestDto productRequestDto) {
        final var product = productMapper.fromDtoToEntity(productRequestDto);
        final Product savedProduct = productRepository.save(product);
        final ProductResponseDto productResponseDto = productMapper.fromEntityToDto(savedProduct);
        merge(productResponseDto.id(), productResponseDto);
        log.info("Product successfully inserted:  {}", savedProduct);
        return productResponseDto;
    }


    /**
     * @param productRequestDto
     * @return
     */
    @CachePut(value = {"map-products-cache"}, key = "#id", unless = "#result==null")
    public ProductResponseDto updateProduct(final Long id, final ProductRequestDto productRequestDto) {

        final Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            productMapper.updateFromDto(productRequestDto, product);
            final Product savedProduct = productRepository.save(product);
            log.info("Product successfully updated:  {}", savedProduct);
            final ProductResponseDto productResponseDto = productMapper.fromEntityToDto(savedProduct);
            merge(id, productResponseDto);
            return productResponseDto;
        }
        throw new BadRequestException(messageService.getLocaleMessage("error.message.records.not.found"));
    }

    public Product getReferenceById(final Long id) {
        return productRepository.getReferenceById(id);
    }


}
