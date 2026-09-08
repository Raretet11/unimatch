package com.rar.unimatch.service;

import com.rar.unimatch.error.BadRequestException;
import com.rar.unimatch.model.DTO.UploadProfilePictureUrlRequest;
import com.rar.unimatch.model.DTO.UploadUrlResponse;
import com.rar.unimatch.model.user.User;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http.Method;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;
    private final Clock clock;

    @Value("${minio.link.upload-url.expiry-minutes}")
    private Integer uploadUrlExpiryInMinutes;

    @Value("${minio.link.download-url.expiry-minutes}")
    private Integer downloadUrlExpiryInMinutes;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.max-file-size-bytes}")
    private Long maxFileSizeBytes;

    public UploadUrlResponse generateUploadProfilePictureUrl(UploadProfilePictureUrlRequest request, User user) throws MinioException {
        if (!request.contentType().startsWith("image/")) {
            throw new BadRequestException("Image only");
        }

        String objectKey = generateProfilePictureKey(user);

        if (fileExists(objectKey)) {
            throw new BadRequestException("User already have profile picture");
        }

        PostPolicy postPolicy = new PostPolicy(bucket, ZonedDateTime.now(clock).plusMinutes(uploadUrlExpiryInMinutes));

        postPolicy.addEqualsCondition("key", objectKey);
        postPolicy.addEqualsCondition("Content-Type", request.contentType());
        postPolicy.addContentLengthRangeCondition(0, maxFileSizeBytes);

        Map<String, String> formData = minioClient.getPresignedPostFormData(postPolicy);

        String uploadUrl = endpoint + "/" + bucket + "/";

        return new UploadUrlResponse(uploadUrl, objectKey, formData);
    }

    public String generateProfilePictureKey(User user) {
        return user.getId().toString();
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
                .expiry(downloadUrlExpiryInMinutes, TimeUnit.MINUTES)
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
