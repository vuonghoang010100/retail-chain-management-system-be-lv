package com.example.sales_system.service;

import com.example.sales_system.dto.request.ProductCreateRequest;
import com.example.sales_system.dto.request.ProductUpdateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.ProductResponse;
import com.example.sales_system.entity.tenant.Product;
import com.example.sales_system.enums.ProductStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.ProductMapper;
import com.example.sales_system.repository.tenant.CategoryRepository;
import com.example.sales_system.repository.tenant.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    S3Service s3Service;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<ProductResponse> getAllProducts(Specification<Product> specification, Pageable pageable) {
        log.info("getAllProducts with pageable {}", pageable);

        Page<Product> page = productRepository.findAll(specification, pageable);

        return ListResponse.<ProductResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(productMapper::toProductResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ProductResponse getProduct(Long id) {
        return productMapper.toProductResponse(getProductById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public ProductResponse createProduct(ProductCreateRequest request) {
        Product product = productMapper.toProduct(request);
        // update status
        product.setStatus(ProductStatus.ACTIVE);
        // update category
        if (Objects.nonNull(request.getCategoryId())) {
            var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        // save to check unique contraits
        product = productRepository.save(product);

        // update image
        product.setImageUrl(s3Service.uploadImageBase64(request.getImageBase64()));

        product = productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        var product = getProductById(id);
        productMapper.updateProduct(product, request);
        // update category
        if (Objects.nonNull(request.getCategoryId())) {
            var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        // save to check unique contraits
        product = productRepository.save(product);

        // update image
        if (request.getImageBase64() != null) {
            product.setImageUrl(s3Service.uploadImageBase64(request.getImageBase64()));
        }

        return productMapper.toProductResponse(product);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteProduct(Long id) {
        // TODO: remove image file
        productRepository.deleteById(id);
    }

    // ----- Helper functions -----

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.PRODUCT_NOT_FOUND));
    }

}
