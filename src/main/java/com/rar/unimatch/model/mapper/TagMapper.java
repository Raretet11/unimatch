package com.rar.unimatch.model.mapper;

import org.springframework.stereotype.Component;

import com.rar.unimatch.model.DTO.TagPublicResponse;
import com.rar.unimatch.model.tag.Tag;

@Component
public class TagMapper {
    public TagPublicResponse toPublicResponse(Tag tag) {
        if (tag == null) {
            return null;
        }

        return new TagPublicResponse(
            tag.getId(),
            tag.getName(),
            tag.getUsageCount()
        );
    }
}
