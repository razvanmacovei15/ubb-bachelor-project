package com.maco.followthebeat.service;

import com.maco.followthebeat.config.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String uploadImage(MultipartFile file, String objectKey) throws Exception {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(objectKey)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );
        return objectKey;
    }

    public String generatePresignedUrl(String objectKey) throws Exception {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(minioProperties.getBucketName())
                .object(objectKey)
                .expiry(minioProperties.getPresignedUrlExpiration(), TimeUnit.MINUTES)
                .build()
        );
    }
} 