package com.rar.unimatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rar.unimatch.model.tag.SkillTag;

@Repository
public interface SkillTagMappingRepository extends JpaRepository<SkillTag, Long> {
    @Query("SELECT st.tagId FROM SkillTag st WHERE st.skillId = :skillId")
    List<Long> findTagIdsBySkillId(@Param("skillId") Long skillId);
}
