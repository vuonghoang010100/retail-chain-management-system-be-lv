package com.example.sales_system.controller;

import com.example.sales_system.configuration.TenantContext;
import com.example.sales_system.dto.response.AppResponse;
import com.example.sales_system.exception.AppException;
import com.example.sales_system.exception.AppStatusCode;
import com.example.sales_system.service.S3Service;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "File", description = "The File API.")
public class FileController {
    S3Service s3Service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public AppResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Uploading file");

        if (!s3Service.isValidFileExtension(file))
            throw new AppException(AppStatusCode.INVALID_FILE_EXTENSION);

        if (!s3Service.isValidFileSize(file))
            throw new AppException(AppStatusCode.FILE_MUST_BE_LESS_THAN_10MB);

        String extension = s3Service.getFileExtension(file);
        String table = StringUtils.endsWithIgnoreCase(extension, "pdf") ? "contract" : "product";

        String fileName = s3Service.generateFileName(TenantContext.getTenantId(), table, extension);

        String FileUrl = s3Service.uploadFile(fileName, file);

        return AppResponse.<String>builder()
                .result(FileUrl)
                .build();
    }
}
