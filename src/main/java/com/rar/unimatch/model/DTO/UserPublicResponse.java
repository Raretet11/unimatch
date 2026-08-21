package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import com.rar.unimatch.model.user.Degree;
import com.rar.unimatch.model.user.Sex;

@Schema(description = "Публичная информация о пользователе")
public record UserPublicResponse(
    @Schema(description = "ID пользователя", example = "1")
    Long id,

    @Schema(description = "Имя пользователя", example = "nickname")
    String username,

    @Schema(description = "Имя", example = "Иван")
    String firstname,

    @Schema(description = "Фамилия", example = "Иванов")
    String surname,

    @Schema(description = "Отчество", example = "Иванович")
    String patronymic,

    @Schema(description = "Пол", example = "MALE")
    Sex sex,

    @Schema(description = "Курс", example = "3")
    Integer course,

    @Schema(description = "Степень", example = "BACHELOR")
    Degree degree,

    @Schema(description = "Программа обучения", example = "ПМИ")
    String studyProgram,

    @Schema(description = "Кампус", example = "ВШЭ СПб")
    String campus,
    
    @Schema(description = "Описание пользователя")
    String description
) {}
