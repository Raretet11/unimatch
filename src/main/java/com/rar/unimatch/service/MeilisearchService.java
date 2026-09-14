package com.rar.unimatch.service;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.rar.unimatch.model.DTO.SkillSearchRequest;
import com.rar.unimatch.model.DTO.SkillSearchResponse;
import com.rar.unimatch.model.mapper.SkillMapper;
import com.rar.unimatch.model.skill.RewardType;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.skill.SkillFilterField;
import com.rar.unimatch.model.skill.SkillSearchDocument;
import com.rar.unimatch.model.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeilisearchService {
    private final Client meilisearchClient;
    private final UserService userService;
    private final SkillMapper skillMapper;
    private final ObjectMapper objectMapper;

    private Index skillsIndex;

    private final static String INDEX_NAME = "skills";

    @PostConstruct
    public void init() {
        skillsIndex = meilisearchClient.index(INDEX_NAME);

        String[] searchableAttributes = {
            SkillFilterField.TITLE.field(),
            SkillFilterField.DESCRIPTION.field()
        };
        skillsIndex.updateSearchableAttributesSettings(searchableAttributes);

        String[] filterableAttributes = {
            SkillFilterField.GENDER.field(),
            SkillFilterField.DEGREE.field(),
            SkillFilterField.COURSE.field(),
            SkillFilterField.CAMPUS.field(),
            SkillFilterField.SESSION_TYPE.field(),
            SkillFilterField.IS_ACTIVE.field(),
            SkillFilterField.REWARD_TYPE.field(),
            SkillFilterField.REWARD_AMOUNT_RUB.field()
        };
        skillsIndex.updateFilterableAttributesSettings(filterableAttributes);

        String[] sortableAttributes = {
            SkillFilterField.REWARD_AMOUNT_RUB.field(),
            SkillFilterField.CREATED_AT.field()
        };
        skillsIndex.updateSortableAttributesSettings(sortableAttributes);

        log.info("Meilisearch index initialized");
    }

    public void indexSkill(Skill skill) {
        User user = userService.getById(skill.getUser().getId());
        String jsonDocument = skillMapper.toJson(SkillSearchDocument.fromSkill(skill, user));
        skillsIndex.addDocuments(jsonDocument);

        log.info("Indexed skill {}: {}", skill.getId(), skill.getTitle());
    }

    public void deleteSkill(Long skillId) {
        skillsIndex.deleteDocument(String.valueOf(skillId));
        log.info("Deleted skill {} from Meilisearch", skillId);
    }

    public SkillSearchResponse searchSkills(SkillSearchRequest filter) {
        Index index = meilisearchClient.index(INDEX_NAME);

        List<String> filters = buildFilters(filter);

        SearchRequest.SearchRequestBuilder builder = SearchRequest.builder()
            .q(filter.query() != null ? filter.query() : "")
            .limit(filter.limit())
            .offset(filter.offset());

        if (!filters.isEmpty()) {
            builder.filter(filters.toArray(new String[0]));
        }

        if (filter.sortBy() != null) {
            String order = "desc".equals(filter.sortOrder()) ? "desc" : "asc";
            builder.sort(new String[]{filter.sortBy() + ":" + order});
        }

        log.info("Builder filters: " + filters);

        SearchRequest searchRequest = builder.build();
        Searchable result = index.search(searchRequest);

        List<Long> skills = result.getHits().stream()
            .map(hit -> objectMapper.convertValue(hit, SkillSearchDocument.class).getId())
            .collect(Collectors.toList());

        return SkillSearchResponse.builder()
            .skills(skills)
            .total(skills.size())
            .query(result.getQuery())
            .limit(filter.limit())
            .offset(filter.offset())
            .build();
    }

    private List<String> buildFilters(SkillSearchRequest f) {
        List<String> filters = new ArrayList<>();

        addInFilter(filters, SkillFilterField.GENDER.field(), f.genders());
        addInFilter(filters, SkillFilterField.DEGREE.field(), f.degree());
        addInFilter(filters, SkillFilterField.COURSE.field(), f.courses());
        addInFilter(filters, SkillFilterField.CAMPUS.field(), f.campus());
        addInFilter(filters, SkillFilterField.SESSION_TYPE.field(), f.sessionTypes());
        addInFilter(filters, SkillFilterField.REWARD_TYPE.field(), f.rewardTypes());

        if (f.isActive() != null) {
            filters.add(SkillFilterField.IS_ACTIVE.field() + " = " + f.isActive());
        }

        boolean moneyOnly = f.rewardTypes() != null
            && f.rewardTypes().contains(RewardType.MONEY.toString());

        if (moneyOnly && f.minReward() != null) {
            filters.add(SkillFilterField.REWARD_AMOUNT_RUB.field() + " >= " + f.minReward().doubleValue());
        }
        if (moneyOnly && f.maxReward() != null) {
            filters.add(SkillFilterField.REWARD_AMOUNT_RUB.field() + " <= " + f.maxReward().doubleValue());
        }

        return filters;
    }

    private void addInFilter(List<String> filters, String field, List<?> values) {
        if (values == null || values.isEmpty()) return;

        String joined = values.stream()
            .map(v -> v instanceof String s ? "'" + s + "'" : String.valueOf(v))
            .collect(Collectors.joining(", "));

        filters.add(field + " IN [" + joined + "]");
    }
}
