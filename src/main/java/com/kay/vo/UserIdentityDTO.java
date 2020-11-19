package com.kay.vo;

import com.kay.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserIdentityDTO {
    private final String userName;
    private final Integer userId;
    private Role role;
}
