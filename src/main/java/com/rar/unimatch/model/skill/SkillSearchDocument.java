package com.rar.unimatch.model.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillSearchDocument {
    private Long id;
    private String title;
    private String description;

    public static SkillSearchDocument fromSkill(Skill skill) {
        return SkillSearchDocument.builder()
            .id(skill.getId())
            .title(skill.getTitle())
            .description(skill.getDescription() != null ? skill.getDescription() : "")
            .build();
    }
}
