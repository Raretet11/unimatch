package com.rar.unimatch.properties;

public interface DTOValidationProperties {
    int PASSWORD_MIN = 8;
    int PASSWORD_MAX = 64;
    int USERNAME_MIN = 4;
    int USERNAME_MAX = 64;
    int EMAIL_MAX = 255;

    String WRONG_PASSWORD_SIZE_ERROR = "Длина пароля должна быть от " + PASSWORD_MIN + " до " + PASSWORD_MAX;
    String WRONG_USENAME_SIZE_ERROR = "Длина имени должна быть от " + USERNAME_MIN + " до " + USERNAME_MAX;
    String WRONG_EMAIL_SIZE_ERROR = "Длина почты должна быть до " + EMAIL_MAX + " симполов";
    String WRONG_EMAIL_TYPE_ERROR = "email должен иметь вид example@example.com";

    String USERNAME_REGEX = "^[a-zA-Z0-9]+$";
    String WRONG_USERNAME_TYPE_ERROR = "username должен состоять только из букв и цифр";
}
