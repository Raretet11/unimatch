package com.rar.unimatch.model.DTO;

import com.rar.unimatch.properties.DTOValidationProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
    name = "SignUpRequest"
)
public record SignUpRequest(
    @Size(
        min = DTOValidationProperties.USERNAME_MIN,
        max = DTOValidationProperties.USERNAME_MAX,
        message = DTOValidationProperties.WRONG_USENAME_SIZE_ERROR
    )
    @Pattern(
        regexp = DTOValidationProperties.USERNAME_REGEX,
        message = DTOValidationProperties.WRONG_USERNAME_TYPE_ERROR
    )
    String username,

    @Size(
        max = DTOValidationProperties.EMAIL_MAX,
        message = DTOValidationProperties.WRONG_EMAIL_SIZE_ERROR
    )
    @Email(message = DTOValidationProperties.WRONG_EMAIL_TYPE_ERROR)
    String email,

    @Size(
        min = DTOValidationProperties.PASSWORD_MIN,
        max = DTOValidationProperties.PASSWORD_MAX,
        message = DTOValidationProperties.WRONG_PASSWORD_SIZE_ERROR
    )
    String password
) {}
