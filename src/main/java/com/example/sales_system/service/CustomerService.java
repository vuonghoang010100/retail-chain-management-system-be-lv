package com.example.sales_system.service;

import com.example.sales_system.dto.request.CustomerCreateRequest;
import com.example.sales_system.dto.request.CustomerUpdateRequest;
import com.example.sales_system.dto.response.CustomerResponse;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.Customer;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.CustomerMapper;
import com.example.sales_system.repository.tenant.CustomerRepository;
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
public class CustomerService {
    private final CustomerMapper customerMapper;
    CustomerRepository customerRepository;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<CustomerResponse> getAllCustomerResponses(Specification<Customer> spec, Pageable pageable) {
        log.debug("getAllCustomerResponses");

        Page<Customer> page = customerRepository.findAll(spec, pageable);

        return ListResponse.<CustomerResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(customerMapper::toCustomerResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public CustomerResponse getCustomerResponse(Long id) {
        return customerMapper.toCustomerResponse(getCustomerById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        Customer customer = customerMapper.toCustomer(request);
        customer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(customer);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = getCustomerById(id);
        customerMapper.updateCustomer(customer, request);
        customer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // ----- Helper functions -----
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.CUSTOMER_NOT_FOUND));
    }
}
