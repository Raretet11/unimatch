package com.rar.unimatch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rar.unimatch.model.tag.SkillTag;
import com.rar.unimatch.repository.SkillTagMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillTagMapService {
    private final SkillTagMappingRepository mappingRepository;

    public List<SkillTag> addTags(List<Long> tagsId, Long skillId) {
        List<SkillTag> skillTags = tagsId.stream()
            .map(tagId -> {
                SkillTag skillTag = SkillTag.builder().skillId(skillId).tagId(tagId).build();
                return skillTag;
            })
            .collect(Collectors.toList());
        return mappingRepository.saveAll(skillTags);
    }

    public List<Long> getTagsIdBySkillId(Long skillId) {
        return mappingRepository.findTagIdsBySkillId(skillId);
    }
}
