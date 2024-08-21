package com.example.sales_system.service;

import com.example.sales_system.dto.request.VendorCreateRequest;
import com.example.sales_system.dto.request.VendorUpdateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.dto.response.VendorResponse;
import com.example.sales_system.dto.response.VendorWithContractResponse;
import com.example.sales_system.entity.tenant.Vendor;
import com.example.sales_system.enums.VendorStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.VendorMapper;
import com.example.sales_system.repository.tenant.VendorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
public class VendorService {
    VendorRepository vendorRepository;
    VendorMapper vendorMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<VendorResponse> getAllVendors(Specification<Vendor> specification, Pageable pageable) {
        log.info("getAllVendors");

        Page<Vendor> page = vendorRepository.findAll(specification, pageable);

        return ListResponse.<VendorResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(vendorMapper::toVendorResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public VendorWithContractResponse getVendorResponse(Long id) {
        return vendorMapper.toVendorWithContractResponse(getVendorById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public VendorResponse createVendor(VendorCreateRequest request) {
        Vendor vendor = vendorMapper.toVendor(request);
        vendor.setStatus(VendorStatus.ACTIVE);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toVendorResponse(vendor);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public VendorResponse updateVendor(Long id, VendorUpdateRequest request) {
        Vendor vendor = getVendorById(id);
        vendorMapper.updateVendor(vendor, request);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toVendorResponse(vendor);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }

    // ----- Helper functions -----

    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.VENDOR_NOT_FOUND));
    }
}
