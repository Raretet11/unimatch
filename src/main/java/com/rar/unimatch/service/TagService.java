package com.rar.unimatch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rar.unimatch.error.BadRequestException;
import com.rar.unimatch.model.DTO.TagCreateRequest;
import com.rar.unimatch.model.tag.Tag;
import com.rar.unimatch.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;

    public Tag create(TagCreateRequest request) {
        Tag tag = Tag.builder()
            .name(normalize(request.name()))
            .build();

        if (repository.existsByName(tag.getName())) {
            throw new BadRequestException("Tag " + tag.getName() + " already exists");
        }
        return repository.save(tag);
    }

    public List<Tag> findSimilar(String name, double treshold, int limit) {
        return repository.findSimilarTags(name, treshold, limit);
    }

    private String normalize(String name) {
        return name.trim()
                  .toLowerCase()
                  .replaceAll("[^a-zа-я0-9\\s]", " ")
                  .replaceAll("\\s+", " ")
                  .trim();
    }
}
