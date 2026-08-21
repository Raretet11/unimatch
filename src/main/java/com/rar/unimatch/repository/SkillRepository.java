package com.rar.unimatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rar.unimatch.model.skill.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findById(Long id);

    List<Skill> findByUserId(Long userId);
}
