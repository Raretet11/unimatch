package com.rar.unimatch.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.rar.unimatch.model.skill.SkillSearchDocument;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillSearchResponse {
    private List<SkillSearchDocument> skills;
    private Integer total;
    private String query;
    private Integer limit;
    private Integer offset;
}
