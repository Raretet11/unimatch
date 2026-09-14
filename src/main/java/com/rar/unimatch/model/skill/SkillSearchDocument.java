package com.rar.unimatch.model.skill;

import com.rar.unimatch.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillSearchDocument {
    private Long id;
    private String title;
    private String description;

    private String campus;
    private String sessionType;
    private Boolean isActive;
    private String rewardType;
    private Double rewardAmountRub;

    private String gender;
    private String degree;
    private Integer course;

    private String createdAt;

    public static SkillSearchDocument fromSkill(Skill skill, User user) {
        return SkillSearchDocument.builder()
            .id(skill.getId())
            .title(skill.getTitle())
            .description(skill.getDescription())
            .campus(getCampus(skill, user))
            .sessionType(skill.getSessionType() != null
                ? skill.getSessionType().name() : null)
            .isActive(skill.isActive())
            .rewardType(skill.getRewardType() != null
                ? skill.getRewardType().name() : null)
            .rewardAmountRub(skill.getRewardAmountRub() != null
                ? skill.getRewardAmountRub().doubleValue() : null)
            .gender(user.getGender() != null
                ? user.getGender().name() : null)
            .degree(user.getDegree() != null
                ? user.getDegree().name() : null)
            .course(user.getCourse())
            .createdAt(skill.getCreatedAt() != null
                ? skill.getCreatedAt().toString() : null)
            .build();
    }

    private static String getCampus(Skill skill, User user) {
        if (skill == null || skill.getStudyFormat() == null) {
            return null;
        }
        if (skill.getStudyFormat() == StudyFormat.OFFLINE) {
            return user.getCampus();
        }
        return skill.getStudyFormat().toString();
    }
}
