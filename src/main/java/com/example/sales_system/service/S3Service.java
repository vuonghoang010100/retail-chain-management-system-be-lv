package com.example.sales_system.service;


import com.example.sales_system.configuration.TenantContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class S3Service {
    S3Client s3Client;

    @NonFinal
    @Value("${aws.s3.bucket}")
    private String bucket;

    @NonFinal
    @Value("${aws.s3.region}")
    private String region;

    @NonFinal
    @Value("${aws.s3.namePrefix}")
    private String namePrefix;

    public String uploadImageBase64(String imageBase64) {
        String type = null;
        String extension = "png";

        if (imageBase64.startsWith("data:image/jpeg;base64,")) {
            type = "image/jpeg";
            extension = "jpg";
        } else if (imageBase64.startsWith("data:image/png;base64,")) {
            type = "image/png";
            extension = "png";
        } else if (imageBase64.startsWith("data:image/jpg;base64,")) {
            type = "image/jpg";
            extension = "jpg";
        }

        if (!StringUtils.hasText(type))
            return null;

        String base64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
        byte[] bI = Base64.getDecoder().decode(base64);
        InputStream fis = new ByteArrayInputStream(bI);

        String fileName = generateFileName(TenantContext.getTenantId(), "product", extension);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(type)
                .contentLength((long) bI.length)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        try {
            s3Client.putObject(objectRequest, RequestBody.fromInputStream(fis, bI.length));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

        return getFileUrl(fileName);
    }


    public String uploadFile(String fileName, MultipartFile multipartFile) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        try {
            File file = convertMultiPartToFile(multipartFile);
            s3Client.putObject(objectRequest, RequestBody.fromFile(file));
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return getFileUrl(fileName);
    }

    public boolean deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    public String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    public String generateFileName(String tenant, String table, String fileNameExtension) {
        return namePrefix + "_" + tenant + "_" + table + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "." + fileNameExtension;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
