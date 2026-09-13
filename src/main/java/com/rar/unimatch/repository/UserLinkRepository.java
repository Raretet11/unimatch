package com.rar.unimatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rar.unimatch.model.user.UserLink;

public interface UserLinkRepository extends JpaRepository<UserLink, Long> {
    List<UserLink> findByUserId(Long userId);
}
