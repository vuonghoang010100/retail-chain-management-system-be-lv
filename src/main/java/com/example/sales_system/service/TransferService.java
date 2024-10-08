package com.example.sales_system.service;

import com.example.sales_system.dto.request.TransferDetailCreationRequest;
import com.example.sales_system.dto.request.TransferUpdateRequest;
import com.example.sales_system.dto.response.ListResponse;
import com.example.sales_system.entity.tenant.*;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.mapper.TransferMapper;
import com.example.sales_system.repository.tenant.BatchRepository;
import com.example.sales_system.repository.tenant.TransferDetailRepository;
import com.example.sales_system.repository.tenant.TransferRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransferService {
    private final BatchRepository batchRepository;
    TransferRepository transferRepository;
    TransferDetailRepository transferDetailRepository;

    StoreService storeService;
    EmployeeService employeeService;
    BatchService batchService;

    TransferMapper transferMapper;

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public ListResponse<TransferResponse> getAll(Specification<Transfer> specification, Pageable pageable) {
        Page<Transfer> page = transferRepository.findAll(specification, pageable);

        return ListResponse.<TransferResponse>builder()
                .size(page.getSize())
                .page(page.getNumber() + 1)
                .total(page.getTotalElements())
                .numOfElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .data(page.getContent().stream().map(transferMapper::toResponse).collect(Collectors.toList()))
                .build();
    }

    @Transactional(transactionManager = "tenantTransactionManager", readOnly = true)
    public TransferResponse getById(Long id) {
        return transferMapper.toResponse(getTransferById(id));
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public TransferResponse create(TransferCreationRequest request) {
        validateDetail(request.getDetails());

        Transfer transfer = new Transfer();

        transfer.setStatus("Đang vận chuyển");
        transfer.setNote(request.getNote());

        // employee
        var employee = employeeService.getEmployeeById(request.getEmployeeId());
        transfer.setEmployee(employee);

        // store
        var fromStore = storeService.getStoreById(request.getFromStoreId());
        transfer.setFromStore(fromStore);

        var toStore = storeService.getStoreById(request.getToStoreId());
        transfer.setToStore(toStore);

        // details
        var details = request.getDetails()
                .stream()
                .map(detailRequest -> {
                    Batch batch = batchService.getBatchById(detailRequest.getBatchId());

                    if (!batch.getStore().getId().equals(fromStore.getId())) {
                        throw new AppException(AppStatusCode.BATCH_MUST_BE_IN_THE_SAME_STORE);
                    }

                    return TransferDetail.builder()
                            .product(batch.getProduct())
                            .fromBatch(batch)
                            .quantity(detailRequest.getQuantity())
                            .build();
                })
                .toList();

        if (details.isEmpty())
            throw new AppException(AppStatusCode.TRANSFER_DETAIL_MUST_NOT_BE_EMPTY);

        // save
        transfer.setDetails(new HashSet<>(details));
        transfer = transferRepository.save(transfer);

        // save details
        Transfer finalTransfer = transfer;
        details.forEach(detail -> {
           detail.setTransfer(finalTransfer);

           detail = transferDetailRepository.save(detail);

            var fromBatch = detail.getFromBatch();
            fromBatch.setQuantity(fromBatch.getQuantity() - detail.getQuantity());
            batchService.updateBatch(fromBatch);
        });

        return transferMapper.toResponse(finalTransfer);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    public TransferResponse update(Long id, TransferUpdateRequest request) {
        validateStatus(request.getStatus());

        Transfer transfer = getTransferById(id);
        transfer.setNote(request.getNote());

        if (transfer.getStatus().equals("Hoàn thành"))
            throw new AppException(AppStatusCode.CANNOT_UPDATE_COMPLETE_TRANSFER);

        if (transfer.getStatus().equals("Đã hủy"))
            throw new AppException(AppStatusCode.CANNOT_UPDATE_CANCEL_TRANSFER);

        if (request.getStatus().equals("Hoàn thành")) {
            // update pending -> complete
            transfer.getDetails().forEach(detail -> {
                var fromBatch = detail.getFromBatch();
                Batch toBatch = Batch.builder()
                        .quantity(detail.getQuantity())
                        .purchaseAmount(detail.getQuantity())
                        .purchasePrice(fromBatch.getPurchasePrice())
                        .mfg(fromBatch.getMfg())
                        .exp(fromBatch.getExp())
                        .store(transfer.getToStore())
                        .product(fromBatch.getProduct())
                        .build();
                toBatch = batchRepository.save(toBatch);
                detail.setToBatch(toBatch);
                transferDetailRepository.save(detail);
            });
        }
        else if (request.getStatus().equals("Đã hủy")) {
            // update pending -> cancel
            transfer.getDetails().forEach(detail -> {
                var fromBatch = detail.getFromBatch();
                fromBatch.setQuantity(fromBatch.getQuantity() + detail.getQuantity());
                batchService.updateBatch(fromBatch);
            });
        }

        transfer.setStatus(request.getStatus());

        return transferMapper.toResponse(transferRepository.save(transfer));
    }


    // ----- Helper functions -----
    public Transfer getTransferById(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new AppException(AppStatusCode.TRANSFER_NOT_FOUND));
    }

    public void validateStatus(String status) {
        switch (status) {
            case "Hoàn thành", "Đã hủy", "Đang vận chuyển" -> {
                return;
            }
        }
        throw new AppException(AppStatusCode.INVALID_TRANSFER_STATUS);
    }

    public void validateDetail(List<TransferDetailCreationRequest> details) {
        details.forEach(detail -> {
           var batch = batchService.getBatchById(detail.getBatchId());

            if (batch.getQuantity() < detail.getQuantity()) {
                throw new AppException(AppStatusCode.INVALID_QUANTITY);
            }
        });
    }

}
