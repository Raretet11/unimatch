package com.rar.unimatch.service;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.rar.unimatch.error.ResourceNotFoundException;
import com.rar.unimatch.model.DTO.SkillSearchResponse;
import com.rar.unimatch.model.mapper.SkillMapper;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.skill.SkillSearchDocument;
import com.rar.unimatch.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeilisearchService {
    private final Client meilisearchClient;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final ObjectMapper objectMapper;

    private Index skillsIndex;

    @PostConstruct
    public void init() {
        skillsIndex = meilisearchClient.index("skills");
        String[] searchableAttributes = {"title", "description"};
        skillsIndex.updateSearchableAttributesSettings(searchableAttributes);
        log.info("Meilisearch index initialized");
    }

    @Transactional
    public void indexSkill(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillId));

        String jsonDocument = skillMapper.toJson(SkillSearchDocument.fromSkill(skill));
        skillsIndex.addDocuments(jsonDocument);

        log.info("Indexed skill {}: {}", skillId, skill.getTitle());
    }

    @Transactional
    public void updateSkill(Long skillId) {
        indexSkill(skillId);
        log.info("Updated skill {} in Meilisearch", skillId);
    }

    @Transactional
    public void deleteSkill(Long skillId) {
        skillsIndex.deleteDocument(String.valueOf(skillId));
        log.info("Deleted skill {} from Meilisearch", skillId);
    }

    public SkillSearchResponse searchSkills(String query, int limit, int offset) {
        Index index = meilisearchClient.index("skills");

        SearchRequest searchRequest = SearchRequest.builder()
            .q(query)
            .limit(limit)
            .offset(offset)
            .build();

        Searchable result = index.search(searchRequest);

        List<SkillSearchDocument> skills = result.getHits().stream()
            .<SkillSearchDocument>map(hit -> objectMapper.convertValue(hit, SkillSearchDocument.class))
            .collect(Collectors.toList());

        return SkillSearchResponse.builder()
            .skills(skills)
            .total(skills.size())
            .query(result.getQuery())
            .limit(limit)
            .offset(offset)
            .build();
    }
}
