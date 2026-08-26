package com.rar.unimatch.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.rar.unimatch.error.BadRequestException;
import com.rar.unimatch.error.ResourceNotFoundException;
import com.rar.unimatch.model.DTO.UserLinkCreateRequest;
import com.rar.unimatch.model.user.Degree;
import com.rar.unimatch.model.user.Sex;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.model.user.UserLink;
import com.rar.unimatch.repository.UserLinkRepository;
import com.rar.unimatch.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final UserLinkRepository linkRepository;

    public User save(User user) {
        log.info("Saved user: [" + user.getEmail() + ", " + user.getUsername() + ", " + user.getId() + "]");
        return repository.save(user);
    }

    public void create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("User with name: \"" + user.getUsername() + "\" already exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("User with email: \"" + user.getEmail() + "\" already exists");
        }

        save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with name: \"" + username + "\" not found"));
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id \"" + id + "\" not found"));
    }

    public User patchUserParams(Map<String, Object> updates, User user) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "firstname" -> user.setFirstname((String) value);
                case "surname" -> user.setSurname((String) value);
                case "patronymic" -> user.setPatronymic((String) value);
                case "sex" -> user.setSex(Sex.valueOf((String) value));
                case "course" -> user.setCourse((Integer) value);
                case "degree" -> user.setDegree(Degree.valueOf((String) value));
                case "studyProgram" -> user.setStudyProgram((String) value);
                case "campus" -> user.setCampus((String) value);
                case "description" -> user.setDescription((String) value);
                default -> throw new BadRequestException("Can't update field: \"" + key + "\"");
            }
        });
        log.info("User params patch: " + updates);
        return repository.save(user);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public UserLink createUserLink(UserLinkCreateRequest request, User user) {
        UserLink userLink = UserLink.builder()
            .linkName(request.linkName())
            .linkValue(request.linkValue())
            .isPublic(request.isPublic())
            .user(user)
            .build();
        return linkRepository.save(userLink);
    }

    public List<UserLink> getLinks(Long userId) {
        return linkRepository.findByUserId(userId);
    }
}
