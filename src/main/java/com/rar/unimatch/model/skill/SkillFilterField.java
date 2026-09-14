package com.rar.unimatch.model.skill;

public enum SkillFilterField {
    GENDER("gender"),
    DEGREE("degree"),
    COURSE("course"),
    CAMPUS("campus"),
    SESSION_TYPE("sessionType"),
    IS_ACTIVE("isActive"),
    REWARD_TYPE("rewardType"),
    REWARD_AMOUNT_RUB("rewardAmountRub"),
    CREATED_AT("createdAt"),
    TITLE("title"),
    DESCRIPTION("description");

    private final String field;

    SkillFilterField(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }
}
