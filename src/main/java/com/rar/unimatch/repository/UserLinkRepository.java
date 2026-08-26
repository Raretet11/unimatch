package com.rar.unimatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rar.unimatch.model.user.UserLink;

@Repository
public interface UserLinkRepository extends JpaRepository<UserLink, Long> {
    List<UserLink> findByUserId(Long userId);
}
