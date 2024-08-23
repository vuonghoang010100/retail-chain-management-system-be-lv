package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.ProductCreateRequest;
import com.example.sales_system.dto.request.ProductUpdateRequest;
import com.example.sales_system.dto.response.ProductResponse;
import com.example.sales_system.entity.tenant.Batch;
import com.example.sales_system.entity.tenant.Product;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toProduct(ProductCreateRequest request);

    @Mapping(source = "batchs", target = "stock", qualifiedByName = "calculateStock")
    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

    @Named("calculateStock")
    default Long calculateStock(Set<Batch> batches) {
        return batches
                .stream()
                .reduce(0L, (acc, batch) -> acc + batch.getQuantity(), Long::sum);
    }
}
