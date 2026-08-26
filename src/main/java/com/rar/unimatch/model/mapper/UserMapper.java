package com.rar.unimatch.model.mapper;

import org.springframework.stereotype.Component;

import com.rar.unimatch.model.DTO.UserLinkPublicResponse;
import com.rar.unimatch.model.DTO.UserPrivateResponse;
import com.rar.unimatch.model.DTO.UserPublicResponse;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.model.user.UserLink;

@Component
public class UserMapper {
    public UserPublicResponse toPublicResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserPublicResponse(
            user.getId(),
            user.getUsername(),
            user.getFirstname(),
            user.getSurname(),
            user.getPatronymic(),
            user.getSex(),
            user.getCourse(),
            user.getDegree(),
            user.getStudyProgram(),
            user.getCampus(),
            user.getDescription()
        );
    }

    public UserPrivateResponse toPrivateResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserPrivateResponse(
            user.getEmail()
        );
    }

    public UserLinkPublicResponse toPublicResponse(UserLink link) {
        if (link == null) {
            return null;
        }

        return new UserLinkPublicResponse(
            link.getId(),
            link.getLinkName(),
            link.getLinkValue(),
            link.getIsPublic()
        );
    }
}
