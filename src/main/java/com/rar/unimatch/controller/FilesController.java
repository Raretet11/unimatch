package com.rar.unimatch.controller;

import com.rar.unimatch.model.DTO.UploadUrlRequest;
import com.rar.unimatch.model.DTO.UploadUrlResponse;
import com.rar.unimatch.service.MinioService;
import com.rar.unimatch.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FilesController {
    private final MinioService minioService;
    private final UserService userService;

    @Operation(summary = "Получение ссылки для прямой загрузки файлов на сервер")
    @GetMapping("/url/upload")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public UploadUrlResponse getUploadUrl(@Valid @RequestBody UploadUrlRequest request) throws MinioException {
        return minioService.generateUploadUrl(request, userService.getCurrentUser());
    }

    @Operation(summary = "Проверка на наличие файла")
    @GetMapping("/exists")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public Map<String, Boolean> checkFileExists(@RequestParam String objectKey) throws MinioException {
        boolean exists = minioService.fileExists(objectKey);
        return Map.of("exists", exists);
    }

    @Operation(summary = "Получение ссылки для прямого скачивания файла с сервера")
    @GetMapping("/url/download")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public Map<String, String> getDownloadUrl(@RequestParam String key) throws MinioException {
        String downloadUrl = minioService.getDownloadUrl(key);
        return Map.of(
            "downloadUrl", downloadUrl,
            "objectKey", key
        );
    }

    @Operation(summary = "Удаление файла")
    @DeleteMapping("/{key}")
    @CircuitBreaker(name = "minio")
    @Retry(name = "default")
    public ResponseEntity<Void> deleteFile(@PathVariable String key) throws MinioException {
        log.info("Deleting file: {}", key);
        minioService.deleteFile(key);
        return ResponseEntity.noContent().build();
    }
}
