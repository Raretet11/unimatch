package com.rar.unimatch.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rar.unimatch.error.BadRequestException;
import com.rar.unimatch.model.DTO.SkillCreateRequest;
import com.rar.unimatch.model.skill.RewardType;
import com.rar.unimatch.model.skill.SessionType;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.skill.StudyFormat;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {
    private final SkillRepository repository;

    public Skill create(SkillCreateRequest request, User user) {
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

    public Skill patchSkillParams(Map<String, Object> updates, Long skillId) {
        Skill skill = repository.getReferenceById(skillId);
        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> skill.setTitle((String) value);
                case "description" -> skill.setDescription((String) value);
                case "studyFormat" -> skill.setStudyFormat(StudyFormat.valueOf((String) value));
                case "sessionType" -> skill.setSessionType(SessionType.valueOf((String) value));
                case "rewardType" -> skill.setRewardType(RewardType.valueOf((String) value));
                case "rewardAmountRub" -> skill.setRewardAmountRub((BigDecimal) value);
                default -> throw new BadRequestException("Can't update field " + key);
            }
        });
        log.info("Patch params {} for: {}", updates.toString(), skillId);
        return repository.save(skill);
    }
}
