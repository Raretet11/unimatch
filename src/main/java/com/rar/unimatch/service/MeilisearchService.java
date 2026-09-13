package com.rar.unimatch.service;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.rar.unimatch.model.DTO.SkillSearchResponse;
import com.rar.unimatch.model.mapper.SkillMapper;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.skill.SkillSearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeilisearchService {
    private final Client meilisearchClient;
    private final SkillMapper skillMapper;
    private final ObjectMapper objectMapper;

    private Index skillsIndex;

    private final static String INDEX_NAME = "skills";

    @PostConstruct
    public void init() {
        skillsIndex = meilisearchClient.index(INDEX_NAME);
        String[] searchableAttributes = {"title", "description"};
        skillsIndex.updateSearchableAttributesSettings(searchableAttributes);
        log.info("Meilisearch index initialized");
    }

    public void indexSkill(Skill skill) {
        String jsonDocument = skillMapper.toJson(SkillSearchDocument.fromSkill(skill));
        skillsIndex.addDocuments(jsonDocument);

        log.info("Indexed skill {}: {}", skill.getId(), skill.getTitle());
    }

    public void deleteSkill(Long skillId) {
        skillsIndex.deleteDocument(String.valueOf(skillId));
        log.info("Deleted skill {} from Meilisearch", skillId);
    }

    public SkillSearchResponse searchSkills(String query, int limit, int offset) {
        Index index = meilisearchClient.index(INDEX_NAME);

        SearchRequest searchRequest = SearchRequest.builder()
            .q(query)
            .limit(limit)
            .offset(offset)
            .build();

        Searchable result = index.search(searchRequest);

        List<Long> skills = result.getHits().stream()
            .<Long>map(hit -> objectMapper.convertValue(hit, SkillSearchDocument.class).getId())
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
