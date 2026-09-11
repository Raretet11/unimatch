package com.rar.unimatch.controller;

import com.rar.unimatch.model.DTO.UploadProfilePictureUrlRequest;
import com.rar.unimatch.model.DTO.UploadUrlResponse;
import com.rar.unimatch.service.MinioService;
import com.rar.unimatch.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
@Tag(name = "Images")
@RequiredArgsConstructor
@Slf4j
public class ImagesController {
    private final MinioService minioService;
    private final UserService userService;

    @Operation(summary = "Получение ссылки для прямой загрузки аватарки на сервер")
    @GetMapping("/profiles/url/upload")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public UploadUrlResponse getUploadUrl(@Valid @RequestBody UploadProfilePictureUrlRequest request) throws MinioException {
        return minioService.generateUploadProfilePictureUrl(request, userService.getCurrentUser());
    }

    @Operation(summary = "Проверка на наличие картинки")
    @GetMapping("/profiles/exists")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public Map<String, Boolean> checkFileExists(@RequestParam String objectKey) throws MinioException {
        boolean exists = minioService.fileExists(objectKey);
        return Map.of("exists", exists);
    }

    @Operation(summary = "Получение ссылки для прямого скачивания картинки с сервера")
    @GetMapping("/profiles/url/download")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public Map<String, String> getDownloadUrl(@RequestParam String key) throws MinioException {
        String downloadUrl = minioService.getDownloadUrl(key);
        return Map.of(
            "downloadUrl", downloadUrl,
            "objectKey", key
        );
    }

    @Operation(summary = "Удаление аватарки пользователем")
    @DeleteMapping("/profiles/me")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public void deleteFile() throws MinioException {
        String key = minioService.generateProfilePictureKey(userService.getCurrentUser());
        minioService.deleteFile(key);
    }
}
