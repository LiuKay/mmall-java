package com.kay.vo;

import com.kay.domain.Role;
import com.kay.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserIdentityDTO {
    private final String userName;
    private final Integer userId;
    private Role role;

    public static UserIdentityDTO fromUser(User user) {
        return new UserIdentityDTO(user.getUsername(), user.getId(), user.getRole());
    }
}
