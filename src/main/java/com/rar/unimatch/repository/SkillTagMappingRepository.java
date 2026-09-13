package com.rar.unimatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rar.unimatch.model.tag.SkillTag;
import com.rar.unimatch.model.tag.SkillTagId;

public interface SkillTagMappingRepository extends JpaRepository<SkillTag, SkillTagId> {
    @Query("SELECT st.tagId FROM SkillTag st WHERE st.skillId = :skillId")
    List<Long> findTagIdsBySkillId(@Param("skillId") Long skillId);
}
