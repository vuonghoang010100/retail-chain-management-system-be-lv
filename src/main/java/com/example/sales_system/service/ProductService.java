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
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<ProductResponse> getAllProducts(Specification<Product> specification, Pageable pageable) {

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
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        product.setCategory(category);
        // save to check unique contraits
        product = saveProduct(product);

        // update image
        // TODO: convert base64 to File
        product.setImageUrl("https://retail-chain-sale-ms.s3.ap-southeast-2.amazonaws.com/sample.jpg");

        product = saveProduct(product);

        return productMapper.toProductResponse(product);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        var product = getProductById(id);
        productMapper.updateProduct(product, request);
        product = saveProduct(product);

        // TODO: check logic here

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

    private Product saveProduct(Product product) {
        try {
            product = productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (sku)")) {
                throw new AppException(AppStatusCode.SKU_ALREADY_EXISTED);
            } else if (StringUtils.containsIgnoreCase(exception.getMessage(), "Key (name)")) {
                throw new AppException(AppStatusCode.NAME_ALREADY_EXISTED);
            }
            log.debug(exception.getMessage());
            throw exception;
        }
        return product;
    }

}
