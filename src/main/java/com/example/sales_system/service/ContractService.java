package com.example.sales_system.service;

import com.example.sales_system.dto.request.ContractCreateRequest;
import com.example.sales_system.dto.request.ContractUpdateRequest;
import com.example.sales_system.dto.response.ContractResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Contract;
import com.example.sales_system.enums.ContractStatus;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.ContractMapper;
import com.example.sales_system.repository.tenant.ContractRepository;
import com.example.sales_system.repository.tenant.VendorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ContractService {
    ContractRepository contractRepository;
    VendorRepository vendorRepository;
    ContractMapper contractMapper;
    private final S3Service s3Service;

    public ListResponse<ContractResponse> getAllContracts(Specification<Contract> specification, Pageable pageable) {
        log.debug("getAllContracts");

        Page<Contract> page = contractRepository.findAll(specification, pageable);

        return ListResponse.<ContractResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(contractMapper::toContractResponse).collect(Collectors.toList()))
                .build();
    }

    public ContractResponse getContract(Long id) {
        return contractMapper.toContractResponse(getContractById(id));
    }

    public ContractResponse createContract(ContractCreateRequest request) {
        Contract contract = contractMapper.toContract(request);
        // update status
        contract.setStatus(ContractStatus.ACTIVE);
        // udpate vendor
        if (Objects.nonNull(request.getVendorId())) {
            var vendor = vendorRepository.findById(request.getVendorId()).orElse(null);
            contract.setVendor(vendor);
        }
        contract = contractRepository.save(contract);
        return contractMapper.toContractResponse(contract);
    }

    public ContractResponse updateContract(Long id, ContractUpdateRequest request) {
        Contract contract = getContractById(id);
        String oldPdfUrl = contract.getPdfUrl();

        contractMapper.updateContract(contract, request);
        // update vendor
        if (Objects.nonNull(request.getVendorId())) {
            var vendor = vendorRepository.findById(request.getVendorId()).orElse(null);
            contract.setVendor(vendor);
        }
        contract = contractRepository.save(contract);
        // delete file if change
        if (Objects.nonNull(oldPdfUrl) && !oldPdfUrl.equals(request.getPdfUrl())) {
            s3Service.deleteFileUrl(oldPdfUrl);
        }
        return contractMapper.toContractResponse(contract);
    }

    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }


    // ----- Helper functions -----

    private Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.CONTRACT_NOT_FOUND));
    }
}
