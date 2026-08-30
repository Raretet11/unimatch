package com.rar.unimatch.service;

import com.rar.unimatch.model.DTO.UploadUrlRequest;
import com.rar.unimatch.model.DTO.UploadUrlResponse;
import com.rar.unimatch.model.user.User;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http.Method;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public UploadUrlResponse generateUploadUrl(UploadUrlRequest request, User user) throws MinioException {
        String objectKey = String.format(
            "%s/%s",
            user.getId(),
            request.fileName()
        );

        String uploadUrl = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .method(Method.PUT)
                .expiry(5, TimeUnit.MINUTES)
                .extraQueryParams(Map.of(
                    "Content-Type", request.contentType()
                ))
                .build()
        );

        log.info("Generated upload URL for key: {}", objectKey);
        return new UploadUrlResponse(uploadUrl, objectKey);
    }

    public boolean fileExists(String objectKey) throws MinioException {
        StatObjectResponse response = minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build()
        );
        return response != null;
    }

    public String getDownloadUrl(String objectKey) throws MinioException {
        String uploadUrl = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .method(Method.GET)
                .expiry(15, TimeUnit.MINUTES)
                .build()
        );
        return uploadUrl;
    }

    public void deleteFile(String objectKey) throws MinioException {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build()
        );
        log.info("Deleted file: {}", objectKey);
    }
}
