package com.rar.unimatch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rar.unimatch.model.DTO.SkillCreateRequest;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository repository;

    public Skill save(SkillCreateRequest request, User user) {
        Skill skill = Skill.builder()
            .user(user)
            .isResponse(request.isResponse())
            .title(request.title())
            .description(request.description())
            .studyFormat(request.studyFormat())
            .sessionType(request.sessionType())
            .rewardType(request.rewardType())
            .rewardAmountRub(request.rewardAmountRub())
            .isActive(true)
            .build();
        return repository.save(skill);
    }

    public List<Skill> getSkillsByUser(User user) {
        return repository.findByUserId(user.getId());
    }
}
