package com.kay.domain;

import io.swagger.annotations.ApiModel;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel(value = "用户Model")
public class User {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Role role;

    private Date createTime;

    private Date updateTime;

    public User() {
    }

    public User(Integer id, String username, String password, String email, String phone, String question,
                String answer, Role role, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
        this.role = role;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}